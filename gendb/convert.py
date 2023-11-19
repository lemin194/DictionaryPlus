import sqlite3
import json
with sqlite3.connect('dict_hh.db') as conn:
    
    cursor = conn.cursor()
    
    res = cursor.execute('PRAGMA table_info(anhviet)')
    field = [x[1] for x in res]
    print(field)
    res = cursor.execute('SELECT * FROM anhviet')
    

    dt = []
    for word in res:
        wd = {} 
        for i in range(len(field)):
            wd[field[i]] = word[i]
        dt.append(wd)
    
    with open('backup.json', 'w', encoding='utf-8') as f:
        json.dump(dt, f)
            

    


