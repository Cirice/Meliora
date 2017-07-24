# gensim modules
import gensim
import yaml
import logging
import numpy as np
from os import path, listdir

from gensim.models import Word2Vec

try:
    logging.basicConfig(level=logging.DEBUG,
                        format='%(asctime)s  %(levelname)-8s %(message)s',
                        datefmt='%Y-%m-%d,%H:%M:%S',
                        filename='../log/trainer.log',
                        filemode='w')
    console = logging.StreamHandler()
    console.setLevel(logging.INFO)
    formatter = logging.Formatter('%(name)-12s: %(levelname)-8s %(message)s')
    console.setFormatter(formatter)
    logging.getLogger('').addHandler(console)

except Exception as exc:
    print(str(exc))


def read_config(file_path : "path of a yaml config file") -> "a Python dictionary or none.":
    logging.warning("Trying to load " + file_path)
    with open(file_path) as stream:
        try:
            return yaml.load(stream)
        except yaml.YAMLError as exc:
            logging.error("Problem in reading the file " + file_path + exc.__str__())


class LineSentence(object):
    def __init__(self, dirname):
        self.dirname = dirname

    def __iter__(self):
        for fname in listdir(self.dirname):
            for line in open(path.join(self.dirname, fname)):
                yield line.split()


if __name__ == "__main__":

    config = read_config(file_path="../conf/default-conf.yml")

    print(config)

    source_dir = config['source_dir']
    sentences = LineSentence(source_dir)
    step = (config['params']['alpha'] - config['params']['min_alpha'])/config['params']['iter']
    alphas = np.arange(config['params']['min_alpha'], config['params']['alpha'] + step, step)

    print(alphas)

    model = gensim.models.Word2Vec(sentences, workers=config['params']['workers'], size=config['params']['size'],
                                   min_count=config['params']['min_count'], alpha=alphas[0], iter=1)

    for i in range(1, len(alphas)):
        model = gensim.models.Word2Vec(sentences, workers=config['params']['workers'], size=config['params']['size'],
                                       min_count=config['params']['min_count'], alpha=alphas[i] , iter=1)

    model.save(config['output_model_path'])

    # model = Word2Vec.load("/media/shagrath/Hoji/gensim-model-v.0.1.0.bin")
    # print(model.wv['خودرو'])
