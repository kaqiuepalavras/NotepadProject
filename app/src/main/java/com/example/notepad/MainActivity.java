// app/java/com/example/notepad/MainActivity.java
package com.example.notepad; // Ajuste o nome do pacote para o seu

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Para mensagens de log (debug)
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.notepad.adapter.NotasAdapter; // Importe seu NotasAdapter
import com.example.notepad.database.NotasDatabaseHelper; // Importe seu NotasDatabaseHelper
import com.example.notepad.model.Nota; // Importe sua classe Nota

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotas;
    private NotasAdapter notasAdapter;
    private List<Nota> notasList;
    private FloatingActionButton fabAddNota;
    private NotasDatabaseHelper databaseHelper; // Instância do seu helper de BD

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Define o layout da tela principal

        // Inicializa o helper do banco de dados
        databaseHelper = new NotasDatabaseHelper(this);

        // Inicializa a lista de notas (vazia por enquanto, será preenchida em onResume)
        notasList = new ArrayList<>();

        // Encontra o RecyclerView no layout
        recyclerViewNotas = findViewById(R.id.recyclerViewNotas);
        // Configura o layout manager (LinearLayoutManager exibe itens em uma única linha ou coluna)
        recyclerViewNotas.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa o adapter com a lista vazia e o define no RecyclerView
        notasAdapter = new NotasAdapter(notasList);
        recyclerViewNotas.setAdapter(notasAdapter);

        // Configura o listener para cliques nos itens da lista
        notasAdapter.setOnItemClickListener(nota -> {
            // Quando um item é clicado, abre a DetalheNotaActivity para edição
            Log.d("MainActivity", "Nota clicada: " + nota.getTitulo()); // Log de debug
            Intent intent = new Intent(MainActivity.this, DetalheNotaActivity.class);
            intent.putExtra("nota", nota); // Passa o objeto Nota inteiro para a próxima Activity
            startActivity(intent);
        });

        // Encontra o Floating Action Button e configura seu listener de clique
        fabAddNota = findViewById(R.id.fabAddNota);
        fabAddNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quando o FAB é clicado, abre a DetalheNotaActivity para criar uma nova nota
                Log.d("MainActivity", "FAB clicado para adicionar nova nota."); // Log de debug
                Intent intent = new Intent(MainActivity.this, DetalheNotaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Este método é chamado sempre que a Activity volta para o primeiro plano
        // É o local ideal para carregar as notas do banco de dados para garantir que a lista esteja atualizada
        carregarNotas();
    }

    private void carregarNotas() {
        notasList.clear(); // Limpa a lista atual
        notasList.addAll(databaseHelper.buscarNotas()); // Carrega todas as notas do banco de dados
        notasAdapter.setNotas(notasList); // Atualiza o adapter com as novas notas
        Log.d("MainActivity", "Notas carregadas: " + notasList.size()); // Log de debug
    }
}