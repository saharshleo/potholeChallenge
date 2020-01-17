from flask import render_template, flash, redirect, url_for, request
from app import app, db
from flask_login import login_required, current_user, login_user, logout_user
from datetime import datetime
from app.forms import AdminRegistrationForm, LoginForm
from app.models import Admin
from app.firebase_utils import get_firebase_db, get_firebase_storage
from functools import wraps
from sqlalchemy.exc import IntegrityError

@app.route('/', methods=['GET'])
@app.route('/home')
def home():
    return render_template('home.html', title='Home')

@app.route('/login', methods=['GET'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('home'))

    form_admin = LoginForm()
    return render_template('login.html', form_admin=form_admin, title='Login')

@app.route('/login_admin', methods=['POST'])
def login_doctor():
    if current_user.is_authenticated:
        print("redirected from login doctor")
        return redirect(url_for('home'))

    form_admin = LoginForm()

    if 1 or form_admin.validate_on_submit():
        admin = Admin.query.filter_by(email=form_admin.email.data).first()

        if admin and admin.check_password(password=form_admin.password.data):
            login_user(admin)
            print("user logged in as admin")
            next_page = request.args.get('next')
            return redirect(next_page) if next_page else redirect(url_for('home'))
        else:
            print('Login unsuccessful. Please check mail and password')
            flash('Login unsuccessful. Please check mail and password')
            return redirect(url_for('login'))

    return redirect(url_for('login'))
    
@app.route('/logout')
def logout():
    logout_user()
    return redirect(url_for('home'))

@app.route('/register',methods=['GET'])
def register():
    if current_user.is_authenticated:
        return redirect(url_for('home'))

    form_admin = AdminRegistrationForm()
    return render_template('register.html', form_admin=form_admin, title="Sign Up")

@app.route('/register_admin', methods = ['POST'])
def register_admin():
    form_admin = AdminRegistrationForm()

    if 1 or form_admin.validate_on_submit():
        admin = Admin(name = form_admin.name.data, email = form_admin.email.data)
        admin.set_password(form_admin.password.data)

        db.session.add(admin)
        db.session.commit()
    
    else:
        flash("error in signing up")

    return redirect(url_for('home'))    

@login_required
@app.route('/report')
def report():
    
    return render_template('report.html', title = "Reports")

@login_required
@app.route('/feedback')
def feedback():

    feedback_db = get_firebase_db('/Users-Feedback')
    feedback_list = []
    
    for i in feedback_db.values():
        feedback_list.append([i['city'], i['feed']])

    return render_template('feedback.html', feedback_list = feedback_list, title = "Feedbacks")
