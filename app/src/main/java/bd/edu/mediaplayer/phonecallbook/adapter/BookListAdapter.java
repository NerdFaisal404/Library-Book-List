package bd.edu.mediaplayer.phonecallbook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import bd.edu.mediaplayer.phonecallbook.R;
import bd.edu.mediaplayer.phonecallbook.listner.ItemClickListener;
import bd.edu.mediaplayer.phonecallbook.model.BookList;


public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.MyViewHolder> implements Filterable {
    private List<BookList> bookLists;
    private List<BookList> filerBookLists;
    private Context mContext;
    private ItemClickListener mItemClickListener;

    public BookListAdapter(List<BookList> bookLists, Context context, ItemClickListener itemClickListener) {
        this.bookLists = bookLists;
        this.filerBookLists = bookLists;
        this.mContext = context;
        this.mItemClickListener = itemClickListener;

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // set the data in items
        BookList category = filerBookLists.get(position);

        holder.name.setText(category.getBookName());
        holder.author.setText(category.getAuthor());
        holder.remarks.setText(category.getRemarks());
        holder.ivDelete.setTag(category);
        holder.ivEdit.setTag(category);

    }


    @Override
    public int getItemCount() {
        return filerBookLists.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filerBookLists = bookLists;
                } else {
                    List<BookList> filteredList = new ArrayList<>();
                    for (BookList row : bookLists) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBookName().toLowerCase().contains(charString.toLowerCase()) || row.getAuthor().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    filerBookLists = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filerBookLists;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filerBookLists = (ArrayList<BookList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // init the item view's
        TextView name, author, remarks;
        LinearLayout layout_main;
        ImageButton ivDelete, ivEdit;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the remarks of item view's
            name = itemView.findViewById(R.id.tv_book_name);
            author = itemView.findViewById(R.id.tv_book_author);
            remarks = itemView.findViewById(R.id.tv_book_remarks);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            layout_main = itemView.findViewById(R.id.lyt_parent);
            layout_main.setOnClickListener(this);
            ivDelete.setOnClickListener(this);
            ivEdit.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            BookList bookList;
            switch (v.getId()) {
                case R.id.iv_delete:
                    bookList = (BookList) v.getTag();

                    mItemClickListener.itemClick(v, bookList,this.getPosition());
                    break;

                case R.id.iv_edit:
                    bookList = (BookList) v.getTag();
                    mItemClickListener.itemClick(v, bookList, this.getPosition());
                    break;
            }

           /* Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(AppConstant.CATEGORY, category.getCategoryTile());
            context.startActivity(intent);
        */
        }
    }

    public void removeAt(int position) {
        bookLists.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, bookLists.size());
    }

    public void updateAt(int updateIndex,BookList bookList) {
        bookLists.set(updateIndex, bookList);
        notifyItemChanged(updateIndex);
    }


}
