package thisis.vegetarian.question.mark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;

public class IVFCategoryAdapter extends RecyclerView.Adapter<IVFCategoryAdapter.ProductHolder> {
    private List<IVF_ProductDataEntity> productDataEntityList = new ArrayList<>();
    private List<String> vegetarian_name ;
    private int[] vegetarian_image = new int[]{R.drawable.icon_vegan,
                                                R.drawable.icon_lacto_vegetarian,
                                                R.drawable.icon_ovo_vegetarian,
                                                R.drawable.icon_lacto_vov_vegetarian,
                                                R.drawable.icon_five_pungent_spices_vegetarian,
                                                R.drawable.icon_meat,
                                                R.drawable.icon_unknow_vegetarian};
    @NonNull
    @NotNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ivf_product, parent, false);
        if (vegetarian_name == null) vegetarian_name = Arrays.asList(parent.getContext().getResources().getStringArray(R.array.vegetarian));
        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductHolder holder, int position) {
        IVF_ProductDataEntity productDataEntity = productDataEntityList.get(position);
        holder.product_name.setText(productDataEntity.getName());
        holder.product_barcode.setText(String.format("%013d",Long.parseLong(productDataEntity.getBarcode())));
        holder.product_vegetarian.setText(productDataEntity.getVegetarian() < vegetarian_name.size() - 1 ? vegetarian_name.get(productDataEntity.getVegetarian()) : vegetarian_name.get(vegetarian_name.size() - 1));
        holder.product_icon.setImageResource(productDataEntity.getVegetarian() < vegetarian_image.length -1 ? vegetarian_image[productDataEntity.getVegetarian()] : vegetarian_image[vegetarian_image.length -1]);
    }

    @Override
    public int getItemCount() {
        return productDataEntityList.size();
    }

    public void setProductDataEntityList(List<IVF_ProductDataEntity> list){
        this.productDataEntityList = list;
        notifyDataSetChanged();
    }

    class ProductHolder extends RecyclerView.ViewHolder{
        private final TextView product_name;
        private final TextView product_barcode;
        private final TextView product_vegetarian;
        private final ImageView product_icon;
        public ProductHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.item_ivf_name);
            product_barcode = itemView.findViewById(R.id.item_ivf_barcode);
            product_vegetarian = itemView.findViewById(R.id.item_ivf_vegetarian);
            product_icon = itemView.findViewById(R.id.item_ivf_icon);
        }
    }
}
