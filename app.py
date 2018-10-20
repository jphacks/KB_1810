# -*- coding: utf-8 -*-
from flask import Flask, render_template, request, jsonify
import mysql.connector

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False #日本語文字化け対策
app.config["JSON_SORT_KEYS"] = False #ソートをそのまま

@app.route('/good',methods=['POST'])
def good():
    name = request.args.get('name')
    age = request.args.get('age')
    data = jsonify({
        "name":name,
        "age":age
    })

    db=mysql.connector.connect(host='localhost',user="root",password="0noLab",charset="utf8")
    cursor= db.cursor()

    cursor.execute("USE eat")
    db.commit()
    #cursor.execute('SELECT* FROM image')
    #cursor.execute("INSERT INTO image (post_id, user_id, score, image_url, lable) values(%d, %d, %d, %s, %d)"
    insert_data= "INSERT INTO image (user_id, score, lable) values(%s, %s, %s)"
    data_list = [
    (4, 15, 78)
    ]
    for image in data_list:
        cursor.execute(insert_data, image)
    #print(sql)
    db.commit()
    cursor.close()
    db.close()
    return data


@app.route('/bad')
def bad():
    name = "Good"
    return name


## おまじない
if __name__ == "__main__":
        app.run(debug=True)
