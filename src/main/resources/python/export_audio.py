from ffmpy3 import FFmpeg
from sys import argv

if __name__ == '__main__':
    input_path = argv[1]
    output_path = argv[2]
    ff = FFmpeg(
        inputs={input_path: None},
        outputs={output_path: ['-vn', '-c:a', 'copy', '-y']}
    )
    ff.run()
