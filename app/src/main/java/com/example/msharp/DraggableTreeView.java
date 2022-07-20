package com.example.msharp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by jbonk on 6/16/2017.
 */

/*The library I used for the main editor UI. My changes appear between lines ~456 -> ~750*/

public class DraggableTreeView extends FrameLayout{

    ScrollView mRootLayout;
    LinearLayout mParentLayout;
    private TreeViewAdapter adapter;

    private BitmapDrawable mHoverCell;
    private Rect mHoverCellCurrentBounds;
    private Rect mHoverCellOriginalBounds;

    private TreeNode mobileNode,lastNode;
    private int sideMargin = 20;
    private enum Drop{above_sibling,below_sibling,child,cancel}
    Drop drop_item;

    private long mPlaceholderCheck = System.currentTimeMillis();
    private long mPlaceholderDelay = new Long(200);
    private int mDownY = -1;
    private int mDownX = -1;
    private int mScrollDownY = -1;
    private int mLastEventX = -1;
    private int mLastEventY = -1;
    public ArrayList<TreeNode> nodeOrder = new ArrayList<>();
    public int maxLevels = -1;
    public boolean makeSiblingAtMaxLevel = true;
    //public MyKeyboard keyboard = null;

    private View mobileView;
    private boolean mCellIsMobile = false;

    public DraggableTreeView(Context context) {
        super(context);
    }

    public DraggableTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private DragItemCallback mDragItemCallback = new DragItemCallback(){

        @Override
        public void onStartDrag(View item, TreeNode node){

        }

        @Override
        public void onChangedPosition(View item, TreeNode child, TreeNode parent, int position) {

        }

        @Override
        public void onEndDrag(View item, TreeNode child, TreeNode parent, int position) {

        }

    };

    public void setOnDragItemListener(DragItemCallback dragItemCallback){
        mDragItemCallback = dragItemCallback;
    }

    public interface DragItemCallback{
        void onStartDrag(View item,TreeNode node);
        void onChangedPosition(View item,TreeNode child,TreeNode parent,int position);
        void onEndDrag(View item,TreeNode child,TreeNode parent, int position);
    }


    public void setAdapter(TreeViewAdapter adapter){
        this.adapter = adapter;
        this.adapter.setDraggableTreeView(this);
        adapter.setTreeViews();
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged(){
        if(adapter != null) {
            mParentLayout.removeAllViews();
            nodeOrder = new ArrayList<>();
            inflateViews(adapter.root);
        }
    }

    private void inflateViews(TreeNode node){
        if(!node.isRoot()) {
            createTreeItem(node.getView(),node);
        }else{
            ((ViewGroup) node.getView()).removeAllViews();
            mParentLayout.addView((LinearLayout)node.getView());
        }
        for (int i = 0; i < node.getChildren().size(); i++) {
            inflateViews(node.getChildren().get(i));
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRootLayout = new ScrollView(getContext());
        mParentLayout = new LinearLayout(getContext());
        mParentLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mRootLayout.addView(mParentLayout);
        addView(mRootLayout);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean colValue = handleItemDragEvent(event);
        return colValue || super.onInterceptTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean colValue = handleItemDragEvent(event);
        return colValue || super.onTouchEvent(event);
    }
    public long lastDown = 0;

    public boolean handleItemDragEvent(MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                lastDown = System.currentTimeMillis();
                mDownX = (int)event.getRawX();
                mDownY = (int)event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(mDownY == -1){
                    mDownY = (int)event.getRawY();

                }
                if(mDownX == -1){
                    mDownX = (int)event.getRawX();
                }
                if(mScrollDownY == -1){
                    mScrollDownY = mRootLayout.getScrollY();
                }

                mLastEventX = (int) event.getRawX();
                mLastEventY = (int) event.getRawY();
                int deltaX = mLastEventX - mDownX;
                int deltaY = mLastEventY - mDownY;

                if (mCellIsMobile) {
                    int location[] = new int[2];
                    mobileView.getLocationOnScreen(location);
                    int root_location[] = new int[2];
                    mRootLayout.getLocationOnScreen(root_location);
                    int offsetX = deltaX-root_location[0];
                    int offsetY = location[1]+deltaY-root_location[1]+mRootLayout.getScrollY()-mScrollDownY;
                    mHoverCellCurrentBounds.offsetTo(offsetX,
                            offsetY);
                    mHoverCell.setBounds(rotatedBounds(mHoverCellCurrentBounds,0.0523599f));
                    invalidate();
                    handleItemDrag();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:

                touchEventsCancelled();
                break;
            case MotionEvent.ACTION_CANCEL:
                touchEventsCancelled();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            default:
                break;
        }
        return false;
    }

    private void handleItemDrag(){
        LinearLayout layout = ((LinearLayout)adapter.root.getView());
        for(int i =0; i< layout.getChildCount(); i++)
        {
            View view = layout.getChildAt(i);

            int[] location = new int[2];
            view.getLocationInWindow(location);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            Rect viewRect = new Rect(location[0], location[1], location[0]+view.getWidth(), location[1]+view.getHeight());
            Rect outRect = new Rect(0, location[1]-lp.topMargin, Resources.getSystem().getDisplayMetrics().widthPixels, location[1]+view.getHeight()+lp.bottomMargin);

            if(outRect.contains(mLastEventX, mLastEventY))
            {
                //set last position
                int[] root_location = new int[2];
                mRootLayout.getLocationOnScreen(root_location);
                if(root_location[1] > mLastEventY - dpToPx(25)){
                    mRootLayout.smoothScrollBy(0,-10);
                }
                if(root_location[1]+mRootLayout.getHeight() < mLastEventY + dpToPx(25)){
                    mRootLayout.smoothScrollBy(0,10);
                }
                if(mLastEventY < viewRect.top+view.getHeight()/2 && mLastEventX <= mDownX+dpToPx(40)){
                    //above so make sibling
                    if(mPlaceholderCheck+mPlaceholderDelay < System.currentTimeMillis()) {
                        boolean has_changed = false;
                        if(lastNode != null && (drop_item != Drop.above_sibling || lastNode != nodeOrder.get(i))){
                            //Item has changed
                            has_changed = true;
                        }
                        drop_item = Drop.above_sibling;
                        lastNode = nodeOrder.get(i);
                        if(adapter.placeholder.getParent() != null){
                            ((ViewGroup) adapter.placeholder.getParent()).removeView(adapter.placeholder);
                        }
                        if(adapter.bad_placeholder.getParent() != null){
                            ((ViewGroup) adapter.bad_placeholder.getParent()).removeView(adapter.bad_placeholder);
                        }
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 0, 0, 0 );
                        adapter.placeholder.setLayoutParams(layoutParams);
                        adapter.bad_placeholder.setLayoutParams(layoutParams);
                        int pos = ((ViewGroup) lastNode.getView().getParent()).indexOfChild(lastNode.getView());
                        int level = mobileNode.getChildLevel()+lastNode.getLevel();
                        if(maxLevels != -1 && maxLevels < level){
                            ((ViewGroup) lastNode.getView().getParent()).addView(adapter.bad_placeholder, pos);
                            drop_item = Drop.cancel;
                        }else {
                            if(has_changed) {
                                if(i <= nodeOrder.indexOf(mobileNode) || lastNode.getLevel() > mobileNode.getLevel()){
                                    mDragItemCallback.onChangedPosition(mobileNode.getView(), mobileNode, lastNode.getParent(), lastNode.getPosition()+1);
                                }else {
                                    mDragItemCallback.onChangedPosition(mobileNode.getView(), mobileNode, lastNode.getParent(), lastNode.getPosition());
                                }
                            }
                            ((ViewGroup) lastNode.getView().getParent()).addView(adapter.placeholder, pos);
                        }
                        mPlaceholderCheck = System.currentTimeMillis();
                    }
                }else if(mLastEventX >  mDownX+dpToPx(40)) {
                    //make child
                    if(mPlaceholderCheck+mPlaceholderDelay < System.currentTimeMillis()) {
                        TreeNode temp_node = null;
                        boolean has_changed = false;
                        if(lastNode != null && (drop_item != Drop.child || lastNode != nodeOrder.get(i))){
                            //Item has changed
                            has_changed = true;
                            temp_node = lastNode;
                        }
                        drop_item = Drop.child;
                        lastNode = nodeOrder.get(i);
                        if(adapter.placeholder.getParent() != null){
                            ((ViewGroup) adapter.placeholder.getParent()).removeView(adapter.placeholder);
                        }
                        if(adapter.bad_placeholder.getParent() != null){
                            ((ViewGroup) adapter.bad_placeholder.getParent()).removeView(adapter.bad_placeholder);
                        }
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(dpToPx(sideMargin), 0, 0, 0 );
                        adapter.placeholder.setLayoutParams(layoutParams);
                        adapter.bad_placeholder.setLayoutParams(layoutParams);
                        int level = mobileNode.getChildLevel()+lastNode.getLevel()+1;
                        if(maxLevels != -1 && maxLevels < level){
                            ((ViewGroup) lastNode.getView()).addView(adapter.bad_placeholder);
                            drop_item = Drop.cancel;
                        }else {
                            if(has_changed && temp_node != null) {
                                mDragItemCallback.onChangedPosition(mobileNode.getView(), mobileNode, temp_node,0);
                            }
                            ((ViewGroup) lastNode.getView()).addView(adapter.placeholder);
                        }
                        mPlaceholderCheck = System.currentTimeMillis();
                    }
                }else if(mLastEventY > viewRect.bottom){
                    //below so make sibling
                    if(mPlaceholderCheck+mPlaceholderDelay < System.currentTimeMillis()) {
                        boolean has_changed = false;
                        if(lastNode != null && (drop_item != Drop.below_sibling || lastNode != nodeOrder.get(i))){
                            //Item has changed
                            has_changed = true;
                        }
                        drop_item = Drop.below_sibling;
                        lastNode = nodeOrder.get(i);
                        if(adapter.placeholder.getParent() != null){
                            ((ViewGroup) adapter.placeholder.getParent()).removeView(adapter.placeholder);
                        }
                        if(adapter.bad_placeholder.getParent() != null){
                            ((ViewGroup) adapter.bad_placeholder.getParent()).removeView(adapter.bad_placeholder);
                        }
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 0, 0, 0 );
                        adapter.placeholder.setLayoutParams(layoutParams);
                        adapter.bad_placeholder.setLayoutParams(layoutParams);
                        int pos = ((ViewGroup) lastNode.getView().getParent()).indexOfChild(lastNode.getView());
                        int level = mobileNode.getChildLevel()+lastNode.getLevel();
                        if(maxLevels != -1 && maxLevels < level){
                            ((ViewGroup) lastNode.getView().getParent()).addView(adapter.bad_placeholder, pos + 1);
                            drop_item = Drop.cancel;
                        }else {
                            if(has_changed) {
                                if(i <= nodeOrder.indexOf(mobileNode) || lastNode.getLevel() > mobileNode.getLevel()){
                                    mDragItemCallback.onChangedPosition(mobileNode.getView(), mobileNode, lastNode.getParent(), lastNode.getPosition() + 2);
                                }else{
                                    mDragItemCallback.onChangedPosition(mobileNode.getView(), mobileNode, lastNode.getParent(), lastNode.getPosition() + 1);
                                }
                            }
                            ((ViewGroup) lastNode.getView().getParent()).addView(adapter.placeholder, pos + 1);
                        }
                        mPlaceholderCheck = System.currentTimeMillis();
                    }
                }else{
                    if(mPlaceholderCheck+mPlaceholderDelay < System.currentTimeMillis()) {
                        boolean has_changed = false;
                        if(lastNode != null && (drop_item != Drop.below_sibling || lastNode != nodeOrder.get(i))){
                            //Item has changed
                            has_changed = true;
                        }
                        drop_item = Drop.below_sibling;
                        lastNode = nodeOrder.get(i);
                        if(adapter.placeholder.getParent() != null){
                            ((ViewGroup) adapter.placeholder.getParent()).removeView(adapter.placeholder);
                        }
                        if(adapter.bad_placeholder.getParent() != null){
                            ((ViewGroup) adapter.bad_placeholder.getParent()).removeView(adapter.bad_placeholder);
                        }
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 0, 0, 0 );
                        adapter.placeholder.setLayoutParams(layoutParams);
                        adapter.bad_placeholder.setLayoutParams(layoutParams);
                        int pos = ((ViewGroup) lastNode.getView().getParent()).indexOfChild(lastNode.getView());

                        int level = mobileNode.getChildLevel()+lastNode.getLevel();
                        if(maxLevels != -1 && maxLevels < level){
                            ((ViewGroup) lastNode.getView().getParent()).addView(adapter.bad_placeholder, pos + 1);
                            drop_item = Drop.cancel;
                        }else {
                            if(has_changed) {
                                if(i <= nodeOrder.indexOf(mobileNode) || lastNode.getLevel() > mobileNode.getLevel()){
                                    mDragItemCallback.onChangedPosition(mobileNode.getView(), mobileNode, lastNode.getParent(), lastNode.getPosition() + 2);
                                }else{
                                    mDragItemCallback.onChangedPosition(mobileNode.getView(), mobileNode, lastNode.getParent(), lastNode.getPosition() + 1);
                                }
                            }
                            ((ViewGroup) lastNode.getView().getParent()).addView(adapter.placeholder, pos + 1);
                        }
                        mPlaceholderCheck = System.currentTimeMillis();
                    }
                }

            }
        }
    }

    private void touchEventsCancelled() {
        if(mCellIsMobile && mobileNode != null){
            mobileView.setVisibility(VISIBLE);
            mHoverCell = null;
            if(adapter != null) {
                if(adapter.placeholder.getParent() != null) {
                    ((ViewGroup) adapter.placeholder.getParent()).removeView(adapter.placeholder);
                }
                if(adapter.bad_placeholder.getParent() != null) {
                    ((ViewGroup) adapter.bad_placeholder.getParent()).removeView(adapter.bad_placeholder);
                }
                //Make sure it didn't drop on itself
                if(lastNode != mobileNode || drop_item != Drop.cancel) {
                    if (drop_item == Drop.above_sibling) {
                        int pos = lastNode.getPosition();
                        mobileNode.setParent(lastNode.getParent(), pos - 1);
                        mDragItemCallback.onEndDrag(mobileNode.getView(),mobileNode,lastNode,mobileNode.getPosition()+1);
                    } else if (drop_item == Drop.below_sibling) {
                        int pos = lastNode.getPosition();
                        //if it came from below we need to add
                        mobileNode.setParent(lastNode.getParent(), pos);
                        mDragItemCallback.onEndDrag(mobileNode.getView(),mobileNode,lastNode,mobileNode.getPosition()+1);
                    } else if (drop_item == Drop.child) {
                        mobileNode.setParent(lastNode,0);
                        mDragItemCallback.onEndDrag(mobileNode.getView(),mobileNode,lastNode,mobileNode.getPosition()+1);
                    }
                }

                notifyDataSetChanged();
            }
            invalidate();

        }

        mDownX = -1;
        mDownY = -1;
        mScrollDownY = -1;
        mCellIsMobile = false;
    }

    public void createTreeItem(View view, final TreeNode node){
        if(view != null) {
            nodeOrder.add(node);
            final LinearLayout mItem = new LinearLayout(getContext());
            mItem.setOrientation(LinearLayout.VERTICAL);
            if(view.getParent() != null) {
                ViewGroup parent = (ViewGroup) view.getParent();
                parent.removeView(view);
            }
            view.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mobileNode = node;
                    addToView(mItem,node);
                    mobileView = mItem;
                    mDragItemCallback.onStartDrag(mobileNode.getView(),mobileNode);
                    mItem.post(new Runnable() {
                        @Override
                        public void run() {
                            mCellIsMobile = true;
                            mHoverCell = getAndAddHoverView(mobileView,1f);
                            mobileView.setVisibility(INVISIBLE);
                        }
                    });
                    return false;
                }
            });
            final Context concon = this.getContext();
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    long lastDuration = System.currentTimeMillis() - lastDown;
                    if (lastDuration < 200)
                    {
                        /*Handles events when click on a code block. */
                        switch (node.type)
                        {
                            //Click on an if block. Ask for input.
                            case 1:
                                AlertDialog.Builder alert = new AlertDialog.Builder(concon);

                                alert.setTitle("if");


                                // Set an EditText view to get user input
                                final EditText input = new EditText(concon);
                                alert.setView(input);

                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String value = input.getText().toString();

                                            Functions fun = new Functions();
                                        if(value.equals("")) //no argument, reject.
                                        {
                                            Toast toast=Toast.makeText(concon,"Invalid arguments",Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                        else
                                        {

                                        /*This is where the input is checked before it is allowed to be updated to the code block.
                                          Scans the input into its tokens, then checks if it is parsable as a condition. If it is, it is accepted, otherwise it is rejected with an error
                                          in the editor.
                                        */
                                        try {
                                            fun.checkIsValidCondition(value);
                                            node.data = "if " + value;

                                            adapter.setTreeViews();
                                            notifyDataSetChanged();
                                        } catch (Exception e) {
                                            Toast toast=Toast.makeText(concon,e.getMessage(),Toast.LENGTH_SHORT);
                                            toast.show();
                                        }

                                        }



                                    }
                                });

                                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Canceled.
                                    }
                                });

                                alert.show();

                                break;

                                /*Let block clicked*/
                            case 2:
                                AlertDialog.Builder alert2 = new AlertDialog.Builder(concon);

                                alert2.setTitle("let");


                                // Set an EditText view to get user input
                                final EditText input2 = new EditText(concon);
                                alert2.setView(input2);

                                /*Input is now checked*/
                                alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String value = input2.getText().toString();
                                        String[] values = value.split(" ");
                                        Functions fun = new Functions();
                                        char[] chars = values[0].toCharArray();
                                        if(value.equals("")) //no argument
                                        {
                                            Toast toast=Toast.makeText(concon,"Invalid arguments",Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                        else if(value.indexOf(' ') == -1) //only 1 argument
                                        {
                                            Toast toast=Toast.makeText(concon,"Invalid arguments",Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                        else if(!values[1].equals("=")) //second argument was not '='
                                        {
                                            Toast toast=Toast.makeText(concon,"Invalid operator",Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                        else if(fun.isBool(values[0])) //cant be a bool
                                        {
                                            Toast toast=Toast.makeText(concon,"Invalid variable name",Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                        else if(fun.isInteger(values[0])) //cant be an int
                                        {
                                            Toast toast=Toast.makeText(concon,"Invalid variable name",Toast.LENGTH_LONG);
                                            toast.show();
                                        }

                                        else if (Character.isDigit(chars[0]))
                                        {
                                            Toast toast=Toast.makeText(concon,"Invalid variable name",Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                        else if((values[0]).indexOf('#') != -1)
                                        {
                                            Toast toast=Toast.makeText(concon,"Invalid variable name",Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                        else {




                                            try {
                                                fun.checkValidExpression(values[2]);
                                                node.data = "let " + value;


                                                adapter.setTreeViews();
                                                notifyDataSetChanged();
                                            } catch (Exception e) {
                                                Toast toast = Toast.makeText(concon, e.getMessage(), Toast.LENGTH_LONG);
                                                toast.show();
                                            }

                                        }


                                        // Do something with value!
                                    }
                                });

                                alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Canceled.
                                    }
                                });

                                alert2.show();

                                break;

                                /*While code block clicked.*/
                            case 3:
                                AlertDialog.Builder alert3 = new AlertDialog.Builder(concon);

                                alert3.setTitle("while");


                                // Set an EditText view to get user input
                                final EditText input3 = new EditText(concon);

                                alert3.setView(input3);

                                /*Check its a valid input. */
                                alert3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String value = input3.getText().toString();
                                        if(value.equals("")) //no argument
                                        {
                                            Toast toast=Toast.makeText(concon,"Invalid arguments",Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                        else
                                        {


                                        Functions fun = new Functions();
                                        try {
                                            fun.checkIsValidCondition(value); //check is a valid condition.
                                            node.data = "while " + value;

                                            adapter.setTreeViews();
                                            notifyDataSetChanged();
                                        } catch (Exception e) {
                                            Toast toast=Toast.makeText(concon,e.getMessage(),Toast.LENGTH_LONG);
                                            toast.show();
                                        }

                                        }


                                        // Do something with value!
                                    }
                                });

                                alert3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Canceled.
                                    }
                                });

                                alert3.show();


                                break;

                                /*Special case where a variable is assigned read.*/
                            case 4:
                                AlertDialog.Builder alert4 = new AlertDialog.Builder(concon);

                                alert4.setTitle("read");


                                // Set an EditText view to get user input
                                final EditText input4 = new EditText(concon);
                                alert4.setView(input4);
                                /*get input from user, set it as a variable. */
                                alert4.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String value = input4.getText().toString();

                                        node.data = "read " + value;

                                        adapter.setTreeViews();
                                        notifyDataSetChanged();

                                        // Do something with value!
                                    }
                                });

                                alert4.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Canceled.
                                    }
                                });

                                AlertDialog dialog = alert4.create();
                                Window window = dialog.getWindow();
                                WindowManager.LayoutParams layoutParams=window.getAttributes();
                                layoutParams.gravity = Gravity.BOTTOM;
                                window.setAttributes(layoutParams);
                                dialog.show();

                                //alert4.show();

                                break;

                                /*print code block clicked. */
                            case 5:
                                AlertDialog.Builder alert5 = new AlertDialog.Builder(concon);

                                alert5.setTitle("print");


                                // Set an EditText view to get user input
                                final EditText input5 = new EditText(concon);
                                alert5.setView(input5);

                                alert5.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String value = input5.getText().toString();
                                        if(value.equals("")) //no argument
                                        {
                                            Toast toast=Toast.makeText(concon,"Invalid arguments",Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                        else
                                        {


                                        Functions fun = new Functions();
                                        try {
                                            fun.checkValidExpression(value); //check is a valid expression trying to be printed.
                                            node.data = "print " + value;

                                            adapter.setTreeViews();
                                            notifyDataSetChanged();
                                        } catch (Exception e) {
                                            Toast toast=Toast.makeText(concon,e.getMessage(),Toast.LENGTH_LONG);
                                            toast.show();
                                        }

                                        }

                                        // Do something with value!
                                    }
                                });

                                alert5.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Canceled.
                                    }
                                });

                                alert5.show();


                                break;


                        }


                    }

                }
            });

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(dpToPx(sideMargin*node.getLevel() ), 0, 0, 0 );
            mItem.setLayoutParams(layoutParams);
            mItem.addView(view);
            ((LinearLayout)adapter.root.getView()).addView(mItem);
        }
    }

    private void addToView(LinearLayout linearLayout,TreeNode node){
        for(int i = 0;i < node.getChildren().size();i++) {
            View child = node.getChildren().get(i).getView();
            if(child.getParent().getParent() != null) {
                ((ViewGroup)child.getParent().getParent()).removeView((View) child.getParent());
            }
            linearLayout.addView(((View)child.getParent()));
            addToView(linearLayout,node.getChildren().get(i));
        }
    }

    public int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(mHoverCell != null){
            mHoverCell.draw(canvas);
        }
    }

    private BitmapDrawable getAndAddHoverView(View v, float scale){
        int w = v.getWidth();
        int h = v.getHeight();
        int top = v.getTop();
        int left = v.getLeft();

        Bitmap b = getBitmapWithBorder(v,scale);
        BitmapDrawable drawable = new BitmapDrawable(getResources(),b);
        mHoverCellOriginalBounds = new Rect(left,top,left+w,top+h);
        mHoverCellCurrentBounds = new Rect(mHoverCellOriginalBounds);
        drawable.setBounds(mHoverCellCurrentBounds);
        return drawable;
    }

    private Bitmap getBitmapWithBorder(View v, float scale) {
        Bitmap bitmap = getBitmapFromView(v,0);
        Bitmap b = getBitmapFromView(v,1);
        Canvas can = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAlpha(150);
        can.scale(scale,scale,mDownX,mDownY);
        can.rotate(3);
        can.drawBitmap(b,0,0,paint);
        return bitmap;
    }

    private Bitmap getBitmapFromView(View v, float scale){
        double radians = 0.0523599f;
        double s = Math.abs(Math.sin(radians));
        double c = Math.abs(Math.cos(radians));
        int width = (int)(v.getHeight()*s + v.getWidth()*c);
        int height = (int)(v.getWidth()*s + v.getHeight()*c);
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(scale,scale);
        v.draw(canvas);
        return bitmap;
    }

    private Rect rotatedBounds(Rect tmp,double radians){
        double s = Math.abs(Math.sin(radians));
        double c = Math.abs(Math.cos(radians));
        int width = (int)(tmp.height()*s + tmp.width()*c);
        int height = (int)(tmp.width()*s + tmp.height()*c);

        return new Rect(tmp.left,tmp.top,tmp.left+width,tmp.top+height);
    }

    //adding in stuff for the custom keyboard.



}
