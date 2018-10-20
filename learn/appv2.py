# -*- coding: utf-8 -*-
from flask import Flask, render_template, request, jsonify
import mysql.connector

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False #日本語文字化け対策
app.config["JSON_SORT_KEYS"] = False #ソートをそのまま


@app.route('/good')
def good():
    import main
    name = "Good"
    z = main.color_detection('kondate.jpg')
    return z


## おまじない
if __name__ == "__main__":
        app.run(debug=True)
