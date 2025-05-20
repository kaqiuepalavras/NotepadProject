// app/java/com/example/notepad/DetalheNotaActivity.java
package com.example.notepad; // Ajuste o nome do pacote para o seu

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast; // Para exibir mensagens rápidas

import androidx.appcompat.app.AlertDialog; // Para o diálogo de confirmação de exclusão
import androidx.appcompat.app.AppCompatActivity;

import com.example.notepad.database.NotasDatabaseHelper;
import com.example.notepad.model.Nota;

public class DetalheNotaActivity extends AppCompatActivity {

    private EditText editTextTitulo;
    private EditText editTextConteudo;
    private Button buttonSalvar;
    private Button buttonExcluir;
    private NotasDatabaseHelper databaseHelper;
    private Nota notaAtual; // Para guardar a nota que está sendo editada (se houver)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_nota);

        // Inicializa os componentes de UI
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextConteudo = findViewById(R.id.editTextConteudo);
        buttonSalvar = findViewById(R.id.buttonSalvar);
        buttonExcluir = findViewById(R.id.buttonExcluir);

        // Inicializa o helper do banco de dados
        databaseHelper = new NotasDatabaseHelper(this);

        // Verifica se uma nota foi passada via Intent (significa que estamos editando)
        if (getIntent().hasExtra("nota")) {
            notaAtual = (Nota) getIntent().getSerializableExtra("nota");
            editTextTitulo.setText(notaAtual.getTitulo());
            editTextConteudo.setText(notaAtual.getConteudo());
            buttonExcluir.setVisibility(View.VISIBLE); // Mostra o botão de excluir
        } else {
            // Se nenhuma nota foi passada, é uma nova nota, então o botão excluir fica invisível
            buttonExcluir.setVisibility(View.GONE);
        }

        // Configura o listener para o botão Salvar
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarNota();
            }
        });

        // Configura o listener para o botão Excluir
        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarExclusao();
            }
        });
    }

    private void salvarNota() {
        String titulo = editTextTitulo.getText().toString().trim();
        String conteudo = editTextConteudo.getText().toString().trim();

        if (titulo.isEmpty()) {
            Toast.makeText(this, "O título da nota não pode ser vazio!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (notaAtual == null) {
            // Criar nova nota
            Nota novaNota = new Nota(0, titulo, conteudo); // ID 0 ou qualquer valor, será auto-incrementado pelo BD
            long id = databaseHelper.inserirNota(novaNota);
            if (id > 0) {
                Toast.makeText(this, "Nota salva com sucesso!", Toast.LENGTH_SHORT).show();
                finish(); // Fecha a Activity e volta para a tela anterior (MainActivity)
            } else {
                Toast.makeText(this, "Erro ao salvar nota.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Atualizar nota existente
            notaAtual.setTitulo(titulo);
            notaAtual.setConteudo(conteudo);
            int rowsAffected = databaseHelper.atualizarNota(notaAtual);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Nota atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                finish(); // Fecha a Activity
            } else {
                Toast.makeText(this, "Erro ao atualizar nota.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void confirmarExclusao() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja excluir esta nota?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    if (notaAtual != null) {
                        databaseHelper.excluirNota(notaAtual.getId());
                        Toast.makeText(DetalheNotaActivity.this, "Nota excluída.", Toast.LENGTH_SHORT).show();
                        finish(); // Fecha a Activity
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }
}