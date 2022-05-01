package aplikacja.projektzespokowy2019;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import model.Cwiczenie;
import model.Plan;

public class AdapterDoWyboruPlanu extends ArrayAdapter {
    private final Activity context;
    private final List<Cwiczenie> listaCwiczen;
    private final List<Plan> Plan;

    public AdapterDoWyboruPlanu(Activity context, List<Plan> Plan, List<Cwiczenie> listaCwiczen) {
        super(context, R.layout.layout_wybierz_plan, Plan);
        this.context = context;
        this.Plan = Plan;
        this.listaCwiczen = listaCwiczen;

    }

    public View getView(int position, View view, ViewGroup parent) {

        String[] nameArray = new String[Plan.size()];
        String[] nameCwiczenArray = new String[Plan.get(position).getListaIdCwiczen().size()];

        for(Integer i=0;i<Plan.size();i++)
        {
            nameArray[i]=Plan.get(i).getNazwa().toString();
        }

        for(Integer i=0;i<Plan.get(position).getListaIdCwiczen().size();i++)
        {
            for(Integer j=0;j<listaCwiczen.size();j++)
            {
                if(Plan.get(position).getListaIdCwiczen().get(i).toString().equals(listaCwiczen.get(j).getId().toString()))
                {
                    nameCwiczenArray[i] = listaCwiczen.get(j).getNazwa().toString();
                }
            }
        }

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.layout_wybierz_plan, null, true);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.textViewNamePlan);
        LinearLayout lista = (LinearLayout) rowView.findViewById(R.id.listaCwiczen);

        nameTextField.setText("Nazwa Planu : " + nameArray[position]);

        TextView tr=new TextView(getContext());
        tr.setText("Lista Cwiczeń : ");
        lista.addView(tr);

        for (Integer j=0;j<nameCwiczenArray.length;j++)
        {
            TextView tv=new TextView(getContext());
            tv.setText("•"+nameCwiczenArray[j].toString());
            lista.addView(tv);
        }

        return rowView;
    }
}
