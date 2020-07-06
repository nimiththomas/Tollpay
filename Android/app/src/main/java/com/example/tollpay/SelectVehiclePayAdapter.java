package com.example.tollpay;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectVehiclePayAdapter extends RecyclerView.Adapter<SelectVehiclePayAdapter.MyViewHolder> {
    SelectVehiclePayAdapter selectVehiclePayAdapter;
    SelectVehiclePayBean selectVehiclePayBean;
    List<SelectVehiclePayBean>selectVehiclePayBeanList;
    Context con;
    String tollid;

    public SelectVehiclePayAdapter(List<SelectVehiclePayBean>beanList,Context c,String id)
    {
        this.selectVehiclePayBeanList=beanList;
        this.con=c;
        this.tollid=id;

    }
    @NonNull
    @Override
    public SelectVehiclePayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlevehiclepay,parent,false);
        return new SelectVehiclePayAdapter.MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectVehiclePayAdapter.MyViewHolder holder, int position) {
        selectVehiclePayBean=selectVehiclePayBeanList.get(position);

        holder.vehname.setText(selectVehiclePayBean.getName());
        holder.vehreg.setText(selectVehiclePayBean.getReg());

       if (selectVehiclePayBean.getType().equals("Car/Jeep/Van"))
       {
          //  holder.vehicon.setBackground(con.getDrawable(R.drawable.car));
            holder.vehicon.setBackgroundResource(R.drawable.car);
       }
       else if (selectVehiclePayBean.getType().equals("LCV"))
       {
           //  holder.vehicon.setBackground(con.getDrawable(R.drawable.car));
           holder.vehicon.setBackgroundResource(R.drawable.trailer);
       }




    }

    @Override
    public int getItemCount() {
        return selectVehiclePayBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView vehname,vehreg;
    ImageView vehicon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vehname=itemView.findViewById(R.id.vehname);
            vehreg=itemView.findViewById(R.id.vehreg);
            vehicon=itemView.findViewById(R.id.vehicon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a=getAdapterPosition();
                    selectVehiclePayBean=selectVehiclePayBeanList.get(a);
                    Toast.makeText(con, ""+selectVehiclePayBean.getTypeid(), Toast.LENGTH_SHORT).show();

                    Intent i=new Intent(con,SelectAmount.class);
                    i.putExtra("tollid",tollid);
                    i.putExtra("typeid",selectVehiclePayBean.getTypeid());
                    i.putExtra("vname",selectVehiclePayBean.getName());
                    i.putExtra("vreg",selectVehiclePayBean.getReg());

                    Log.d("zz",tollid);
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}
