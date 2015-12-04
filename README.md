#说点什么
我真的是迫不及待的想要安利你来一起使用并感受它的魅力！

##使用说明
	1.继承 BaseRecyclerAdapter 并提供您的数据类型
	2.如果需要使用消失已久的OnItemClicked的方法，Cmd + N去覆盖OnItemClicked方法。我真的没见过这么好用的OnItemClicked方法，一次性提供给你了位置和数据！
	3.多视图的请继承 BaseDuoItemRecyclerAdapter
	
##关于思想
	从 ListView 转换过来需要习惯这样写
	
	// 这个方法中去初始化视图
	// 例如点击事件
	// 如果点击事件需要位置，视图，或者数据
	// vh都有！
	void onInitViewHolder(VirtualViewHolder vh, View v)
	
	// 这个方法中去根据需要改变视图
	// 就类似于ListView中得getView方法
	void onBindingViewHolder(View v, T data, int position, int type)

##一个例子
	public class VipTableAdapter extends BaseRecyclerAdapter<VIPRoom>{

    OnRushBuyClickedListener mOnRushBuyClicked;

    public void setOnRushBuyClickedListener(OnRushBuyClickedListener listener){
        mOnRushBuyClicked = listener;
    }

    public VipTableAdapter(int layoutResID) {
        super(layoutResID);
    }

    ImageView pic;
    TextView name,position,endTime,initPrice,currentPrice
            ,bidTimes,addPrice,status,addPriceBtn
            ,substractPriceBtn,yourPrice;
    View rushBuyBtn;

    @Override
    void onBindingViewHolder(View v, VIPRoom data, int position, int viewType) {
        findViews(v);
        setViews(data);
    }

    private void setViews(VIPRoom data) {
        Glide.with(fakerContext).load(data.image).placeholder(R.drawable.loading).into(pic);

        name.setText(data.name);
        position.setText(data.descript);
        endTime.setText(data.end_time);
        initPrice.setText(data.init_price);
        currentPrice.setText(data.current_price);
        bidTimes.setText(String.format(fakerContext.getString(R.string.bid_times_txt),data.bid_times));
        addPrice.setText(String.format(fakerContext.getString(R.string.add_limit_txt), data.add_price));
        status.setText(data.status);
        if(TextUtils.isEmpty(data.yourPrice) ){
            yourPrice.setText(data.current_price);
        }else{
            yourPrice.setText(data.yourPrice);
        }

    }

    @Override
    void onInitViewHolder(VirtualViewHolder vh, View v) {
        findViews(v);
        addPriceBtn.setOnClickListener(new AddListener(vh));
        substractPriceBtn.setOnClickListener(new SubtractListener(vh));
        rushBuyBtn.setOnClickListener(new ClickListener(vh));
    }

    void findViews(View v){
        pic = (ImageView) v.findViewById(R.id.wine_pic);
        name = (TextView) v.findViewById(R.id.good_name);
        position = (TextView) v.findViewById(R.id.oringinal_price);
        endTime = (TextView) v.findViewById(R.id.end_in_txt);
        initPrice = (TextView) v.findViewById(R.id.begin_price_txt);
        currentPrice = (TextView) v.findViewById(R.id.current_price_txt);
        bidTimes = (TextView) v.findViewById(R.id.change_times);
        addPrice = (TextView) v.findViewById(R.id.add_limit);
        status = (TextView) v.findViewById(R.id.status);
        addPriceBtn = (TextView) v.findViewById(R.id.add_my_price);
        substractPriceBtn = (TextView) v.findViewById(R.id.substract_my_price);
        yourPrice = (TextView) v.findViewById(R.id.your_price_txt);
        rushBuyBtn = v.findViewById(R.id.get_rush_buy);
    }

    class ClickListener implements View.OnClickListener{

        BaseRecyclerAdapter.VirtualViewHolder holder;

        public ClickListener(BaseRecyclerAdapter.VirtualViewHolder vh){
            holder = vh;
        }

        @Override
        public void onClick(View v) {
            if( mOnRushBuyClicked != null ){
                mOnRushBuyClicked.onRushBuyCLicked((VIPRoom) holder.getData());
            }
        }
    }

    class AddListener implements View.OnClickListener{

        BaseRecyclerAdapter.VirtualViewHolder holder;

        public AddListener(BaseRecyclerAdapter.VirtualViewHolder vh){
            holder = vh;
        }

        @Override
        public void onClick(View v) {
            findViews(holder.getItemView());

            int count = getCount();

            count += Integer.parseInt(((VIPRoom)holder.getData()).add_price);

            ((VIPRoom) holder.getData()).yourPrice = String.valueOf(count);

            yourPrice.setText(String.valueOf(count));

        }
    }

    class SubtractListener implements View.OnClickListener{

        BaseRecyclerAdapter.VirtualViewHolder holder;

        public SubtractListener(BaseRecyclerAdapter.VirtualViewHolder vh){
            holder = vh;
        }

        @Override
        public void onClick(View v) {
            findViews(holder.getItemView());

            int count = getCount();

            count -= Integer.parseInt(((VIPRoom)holder.getData()).add_price);

            if( count <=  Integer.parseInt(((VIPRoom) holder.getData()).init_price) ){
                count = Integer.parseInt(((VIPRoom) holder.getData()).init_price);
            }

            ((VIPRoom) holder.getData()).yourPrice = String.valueOf(count);
            yourPrice.setText(String.valueOf(count));

        }
    }

    int getCount(){
        int count= 0;

        if( yourPrice!= null ){
            try {
                count = Integer.parseInt(yourPrice.getText().toString());
            }catch (NumberFormatException e){
                count = 0;
            }
        }
        return count;
    }

    public interface OnRushBuyClickedListener{
        void onRushBuyCLicked(VIPRoom room);
    }