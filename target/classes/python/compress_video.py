from ffmpy3 import FFmpeg
from sys import argv

if __name__ == '__main__':
    input_path = argv[1]
    output_path = argv[2]
    b = argv[3]
    ff = FFmpeg(
        inputs={input_path: None},
        outputs={output_path: ['-b', str(b)+'k', '-y']}
    )
    ff.run()
