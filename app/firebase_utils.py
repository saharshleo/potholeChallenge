import firebase_admin
from firebase_admin import credentials, db, storage

cred = credentials.Certificate("../potholedetector-33b99-firebase-adminsdk-c256n-f6b2c87d67.json")
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://potholedetector-33b99.firebaseio.com'
})

def get_firebase_db(dict_index):
    return db.reference(dict_index).get()

def get_firebase_storage():
    bucket = storage.bucket("potholedetector-33b99.appspot.com")

    image_bytearray_dict = {}

    for blob in bucket.list_blobs():
        image_bytearray_dict.update({blob.name : blob.download_as_string()})

    return image_bytearray_dict