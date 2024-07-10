package com.example.jila;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    private List<Quiz> quizList;
    private Context context;

    public QuizAdapter(Context context, List<Quiz> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.quizTitleTextView.setText(quiz.getTitle());
        holder.bind(quiz);
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView quizTitleTextView;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizTitleTextView = itemView.findViewById(R.id.quizTitleTextView);
        }

        public void bind(final Quiz quiz) {
            quizTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, QuizActivity.class);
                    intent.putExtra("QUIZ_ID", quiz.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
