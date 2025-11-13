from flask import Flask, render_template, request, redirect, url_for, flash
from flask_sqlalchemy import SQLAlchemy

# Minimal contract:
# - Inputs: HTTP requests to routes '/', '/add', '/toggle/<id>', '/delete/<id>'
# - Outputs: rendered templates or redirects
# - Data shape: Todo(id:int, title:str, completed:bool)
# - Error modes: missing title on add -> flash message; invalid id -> 404

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///db.sqlite3'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SECRET_KEY'] = 'dev-secret'  # change to a secure random value in production

db = SQLAlchemy(app)

class Todo(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(100), nullable=False)
    completed = db.Column(db.Boolean, default=False)

@app.route('/')
def index():
    todo_list=Todo.query.all()
    return render_template('base.html', todo_list=todo_list)


@app.route('/add', methods=['GET', 'POST'])
def add():
    if request.method == 'POST':
        title = request.form.get('title', '').strip()
        if not title:
            flash('Title is required.', 'error')
            return redirect(url_for('add'))

        todo = Todo(title=title)
        db.session.add(todo)
        db.session.commit()
        flash('Todo added.', 'success')
        return redirect(url_for('index'))

    return render_template('add.html')


@app.route('/toggle/<int:todo_id>', methods=['POST'])
def toggle(todo_id):
    todo = Todo.query.get_or_404(todo_id)
    todo.completed = not todo.completed
    db.session.commit()
    return redirect(url_for('index'))


@app.route('/delete/<int:todo_id>', methods=['POST'])
def delete(todo_id):
    todo = Todo.query.get_or_404(todo_id)
    db.session.delete(todo)
    db.session.commit()
    flash('Todo deleted.', 'success')
    return redirect(url_for('index'))

if __name__ == '__main__':
    # ensure database tables exist before starting
    with app.app_context():
        db.create_all()
    app.run(debug=True)