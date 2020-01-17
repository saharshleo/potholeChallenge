
import firebase_admin
from firebase_admin import credentials, storage

cred = credentials.Certificate("../potholedetector-33b99-firebase-adminsdk-c256n-f6b2c87d67.json")
firebase_admin.initialize_app(cred)

bucket = storage.bucket("potholedetector-33b99.appspot.com")
print(bucket)

for fileb in bucket.list_blobs():
    print(fileb.name)
    with open(fileb.name, "wb") as file_obj:
        fileb.download_to_file(file_obj)
