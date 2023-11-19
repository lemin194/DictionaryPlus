import sqlite3

with sqlite3.connect('dict_hh.db') as conn:
    cursor = conn.cursor()
    # cursor.execute('DROP TABLE anhviet')
    # conn.commit()
    res = cursor.execute('SELECT * FROM anhviet LIMIT 5')
    for row in res:
        print(row)