import json
import sqlite3
def convert_json_to_object(json_file_path):
    lst = None 
    with open(json_file_path, 'r', encoding='utf-8') as f: 
        lst = json.load(f)

    return lst 

def init_db_and_table(): 
    with sqlite3.connect('dict_hh.db') as conn:
        cursor = conn.cursor()
        CREATE_TABLE_COMMAND = '''
            CREATE TABLE IF NOT EXISTS anhviet (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                word TEXT NOT NULL, 
                pronunciation TEXT, 
                type TEXT, 
                meaning TEXT NOT NULL);
        '''
        cursor.execute(CREATE_TABLE_COMMAND)
        conn.commit()


def gen_db(lst):
    SQL_COMMAND = "INSERT INTO anhviet (word, pronunciation, type, meaning) VALUES (?, ?, ?, ?) "
    with sqlite3.connect('dict_hh.db') as conn:
        cursor = conn.cursor()
        cursor.executemany(SQL_COMMAND, lst) 
        conn.commit()



if __name__ == '__main__':
    lst = convert_json_to_object('backup.json')
    data = []
    for word in lst:
        tp = []
        for key, val in word.items():
            if key != "id":
                tp.append(val)
        tp = tuple(tp)
        data.append(tp)

    init_db_and_table()
    gen_db(data)