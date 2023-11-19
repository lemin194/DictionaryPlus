import gen
from trim_space import trim_space
from change_path import change_path
if __name__ == '__main__':
    gen.gen_db()
    trim_space()
    SRC = 'dict_hh.db'
    DET = '../src/main/resources/data'
    change_path(SRC,DET)
