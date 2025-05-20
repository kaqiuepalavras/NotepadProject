// app/java/com/example/notepad/database/NotasDatabaseHelper.java
package com.example.notepad.database; // Ajuste o nome do pacote para o seu: com.example.notepad.database

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.notepad.model.Nota; // Importe sua classe Nota

import java.util.ArrayList;
import java.util.List;

public class NotasDatabaseHelper extends SQLiteOpenHelper {

    // Nome do Banco de Dados
    private static final String DATABASE_NAME = "notepad.db";
    // Versão do Banco de Dados (importante para migrações futuras)
    private static final int DATABASE_VERSION = 1;

    // Nome da Tabela de Notas
    private static final String TABLE_NOTES = "notas";
    // Nomes das Colunas da Tabela
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITULO = "titulo";
    private static final String COLUMN_CONTEUDO = "conteudo";

    // Consulta SQL para criar a tabela de notas
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITULO + " TEXT NOT NULL, " +
                    COLUMN_CONTEUDO + " TEXT);"; // Conteúdo pode ser nulo, mas o título não.

    public NotasDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Este método é chamado apenas uma vez, quando o banco de dados é criado pela primeira vez.
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Este método é chamado quando a versão do banco de dados é atualizada.
        // Aqui você lidaria com migrações de dados, se necessário.
        // Por enquanto, apenas apaga a tabela e recria (apenas para desenvolvimento, em produção, faria uma migração)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    // --- Métodos CRUD (Create, Read, Update, Delete) ---

    // Método para inserir uma nova nota
    public long inserirNota(Nota nota) {
        SQLiteDatabase db = this.getWritableDatabase(); // Abre o BD para escrita

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, nota.getTitulo());
        values.put(COLUMN_CONTEUDO, nota.getConteudo());

        // Inserindo a linha. Retorna o ID da nova linha inserida ou -1 em caso de erro.
        long id = db.insert(TABLE_NOTES, null, values);
        db.close(); // Fecha a conexão com o banco de dados
        return id;
    }

    // Método para buscar todas as notas
    public List<Nota> buscarNotas() {
        List<Nota> notasList = new ArrayList<>();
        // Query para selecionar todas as notas
        String selectQuery = "SELECT * FROM " + TABLE_NOTES + " ORDER BY " + COLUMN_ID + " DESC"; // Ordena por ID decrescente para ver as últimas primeiro

        SQLiteDatabase db = this.getReadableDatabase(); // Abre o BD para leitura
        Cursor cursor = db.rawQuery(selectQuery, null); // Executa a query

        // Percorre todas as linhas e adiciona à lista
        if (cursor.moveToFirst()) {
            do {
                Nota nota = new Nota();
                nota.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                nota.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)));
                nota.setConteudo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTEUDO)));

                notasList.add(nota);
            } while (cursor.moveToNext());
        }

        cursor.close(); // Fecha o cursor
        db.close();     // Fecha a conexão
        return notasList;
    }

    // Método para buscar uma única nota por ID
    public Nota buscarNotaPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES,
                new String[]{COLUMN_ID, COLUMN_TITULO, COLUMN_CONTEUDO},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        Nota nota = null;
        if (cursor != null) {
            cursor.moveToFirst();
            nota = new Nota(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTEUDO))
            );
            cursor.close();
        }
        db.close();
        return nota;
    }


    // Método para atualizar uma nota existente
    public int atualizarNota(Nota nota) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, nota.getTitulo());
        values.put(COLUMN_CONTEUDO, nota.getConteudo());

        // Atualizando a linha. Retorna o número de linhas afetadas.
        int rowsAffected = db.update(TABLE_NOTES, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(nota.getId())});
        db.close();
        return rowsAffected;
    }

    // Método para excluir uma nota
    public void excluirNota(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}