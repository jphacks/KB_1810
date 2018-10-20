# -*- coding: utf-8 -*-
from flask import Flask, render_template, request, jsonify
app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False #日本語文字化け対策
app.config["JSON_SORT_KEYS"] = False #ソートをそのまま

@app.route('/')
def hello():
    name = "Hello World"
    return name

@app.route('/path')
def path():
    name = request.args.get('url')
    #age = request.args.get('age')
    data = jsonify({
        "url":name,
        #"age":age
    })
    return data

@app.route('/test',methods = ['GET', 'POST'])
def test():
    if request.method == 'POST':
       ao = request.args.get('tes')
       
       date = jsonify({
        "tes":ao,
        #"age":age
       })
       return date

    else :
       return "non"

## おまじない
if __name__ == "__main__":
    app.run(debug=True)
