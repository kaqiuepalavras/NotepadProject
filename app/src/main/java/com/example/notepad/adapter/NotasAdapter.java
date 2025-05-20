// app/java/com/example/notepad/adapter/NotasAdapter.java
package com.example.notepad.adapter; // Ajuste o nome do pacote para o seu: com.example.notepad.adapter

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R; // Importe a classe R do seu projeto (para recursos de layout)
import com.example.notepad.model.Nota; // Importe sua classe Nota

import java.util.List;

public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.NotaViewHolder> {

    private List<Nota> notas;
    private OnItemClickListener listener; // Interface para cliques nos itens

    // Construtor do Adapter
    public NotasAdapter(List<Nota> notas) {
        this.notas = notas;
    }

    // Interface para lidar com cliques nos itens da lista
    public interface OnItemClickListener {
        void onItemClick(Nota nota);
    }

    // Método para definir o listener de cliques
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Método para atualizar a lista de notas e notificar o RecyclerView
    public void setNotas(List<Nota> novasNotas) {
        this.notas = novasNotas;
        notifyDataSetChanged(); // Notifica o RecyclerView que os dados mudaram e ele precisa se redesenhar
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Este método é chamado quando o RecyclerView precisa de um novo ViewHolder
        // Ele infla (cria) a view do layout 'item_nota.xml' para cada item da lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        // Este método é chamado para exibir os dados em uma posição específica
        // Ele pega os dados da 'Nota' na posição 'position' e os vincula (bind) à view do ViewHolder
        Nota currentNota = notas.get(position);
        holder.textViewTituloNota.setText(currentNota.getTitulo());
        holder.textViewConteudoNota.setText(currentNota.getConteudo());

        // Configura o listener de clique para cada item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentNota);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Retorna o número total de itens na lista de dados
        return notas.size();
    }

    // ViewHolder: Representa cada item da lista (cada CardView com Título e Conteúdo)
    public static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTituloNota;
        TextView textViewConteudoNota;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            // Encontra os TextViews dentro do layout do item_nota.xml
            textViewTituloNota = itemView.findViewById(R.id.textViewTituloNota);
            textViewConteudoNota = itemView.findViewById(R.id.textViewConteudoNota);
        }
    }
}