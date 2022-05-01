package aplikacja.projektzespokowy2019;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import model.Cwiczenie;

public class CustomListAdapter extends ArrayAdapter {
    private final Activity context;
    private final  List<Cwiczenie> Lista;
    private final Integer opcja;

    public CustomListAdapter(Activity context, List<Cwiczenie> _Lista,Integer opcja){
        super(context,R.layout.layout_cwiczenie , _Lista);
        this.context = context;
        this.Lista = _Lista;
        this.opcja = opcja;

    }

    public View getView(int position, View view, ViewGroup parent) {

        String[] nameArray = new String[Lista.size()];
        Integer[] serieArray = new Integer[Lista.size()];
        String[] opisArray = new String[Lista.size()];
        String[] partieArray = new String[Lista.size()];
        for (Integer i = 0; i < Lista.size(); i++) {
            nameArray[i] = Lista.get(i).getNazwa();
            serieArray[i] = Lista.get(i).getSerie();
            opisArray[i] = Lista.get(i).getOpis();
            partieArray[i] = Lista.get(i).getPartiaCiala();
        }

        if (opcja == 0) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.layout_cwiczenie, null, true);
            TextView nameTextField = (TextView) rowView.findViewById(R.id.textViewNameCw);
            TextView serieTextField = (TextView) rowView.findViewById(R.id.textViewSerieCw);
            TextView opisTextField = (TextView) rowView.findViewById(R.id.textViewOpisCw);
            nameTextField.setText(nameArray[position]);
            serieTextField.setText(serieArray[position].toString());
            opisTextField.setText(opisArray[position]);
            return rowView;
        } else if (opcja == 1) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView1 = inflater.inflate(R.layout.layout_cwiczenie_with_delete, null, true);
            TextView nameTextField = (TextView) rowView1.findViewById(R.id.textViewNameCw1);
            TextView serieTextField = (TextView) rowView1.findViewById(R.id.textViewSerieCw1);

            TextView partieTextField = (TextView) rowView1.findViewById(R.id.textViewPartieCw1);
            nameTextField.setText(nameArray[position]);
            serieTextField.setText(serieArray[position].toString());
            partieTextField.setText(partieArray[position]);
            return rowView1;

        }
        return null;
    }
    public List<Cwiczenie> getLista() {
        return Lista;
    }

}
