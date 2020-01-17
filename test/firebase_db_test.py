import firebase_admin
from firebase_admin import credentials, db

cred = credentials.Certificate("../potholedetector-33b99-firebase-adminsdk-c256n-f6b2c87d67.json")

firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://potholedetector-33b99.firebaseio.com'
})

ref = db.reference('/')
print(ref.get()['users'])
