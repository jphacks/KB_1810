# -*- coding: utf-8 -*-
from flask import Flask, render_template, request, jsonify
import mysql.connector
import numpy as np
from PIL import Image
import io


app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False #日本語文字化け対策
app.config["JSON_SORT_KEYS"] = False #ソートをそのまま


@app.route('/good',methods=['POST'])
def good():
    #import main
    img_data = request.files['bmp'].read()
    img = Image.open(io.BytesIO(img_data))
    img.save('test.jpg')

    #z = main.color_detection('kondate.jpg')
    name = request.form["name"]
    return 'Good' + name


## おまじない
if __name__ == "__main__":
        app.run(debug=True)
