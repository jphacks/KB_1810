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
       if ao == 2:
           date = str(
               {
	           "color1":'0',
	           "color2":'10',
	           "color3":'10',
	           "color4":'8',
	           "color5":'10',
	           "per1":'27',
	           "per2":'20',
	           "per3":'20',
	           "per4":'17',
	           "per5":'16',
	           "point1":'3',
       	       }
           )
           return date
       else:
           return str(
              {       
                  "color1":'5',
                  "color2":'0',
                  "color3":'0',
                  "color4":'10',
                  "color5":'8',
                  "per1":'40',
                  "per2":'35',
                  "per3":'17',
                  "per4":'4',
                  "per5":'4',
                  "point1":'1',
              }       
          )       
    else:
       return "non"

## おまじない
if __name__ == "__main__":
   app.run (debug = True)
