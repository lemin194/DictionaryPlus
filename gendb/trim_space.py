import sqlite3

def trim_space():
    with sqlite3.connect('dict_hh.db') as conn:
        cursor = conn.cursor()
        cursor.execute("UPDATE anhviet SET word = RTRIM(word)")
        conn.commit()

