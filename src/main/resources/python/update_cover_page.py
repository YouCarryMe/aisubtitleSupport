from ffmpy3 import FFmpeg
from sys import argv


def generate_cover_page(video_path, page_path, time):
    ff = FFmpeg(
        inputs={video_path: ['-ss', time]},
        outputs={page_path: ['-vframes', '1', '-q:v', '2', '-y']}
    )
    ff.run()

def update_cover_page(video_path, page_path, output_path):
    ff = FFmpeg(
        inputs={video_path: None, page_path: None},
        outputs={output_path: ['-map', '0', '-map', '1', '-c', 'copy', '-c:v:1', 'png', '-disposition:v:1', 'attached_pic', '-y']}
    )
    ff.run()

if __name__ == "__main__":
    video_path = argv[1]
    pattern = argv[2]
    params = argv[3]
    output_path = argv[4]
    assert pattern == 'picture' or pattern == 'time'
    page_path = None
    if pattern == 'picture':
        page_path = params
    elif pattern == 'time':
        page_path = video_path[:-len(video_path.split('.')[-1])-1]+'.jpg'
        generate_cover_page(video_path, page_path, params)
    update_cover_page(video_path, page_path, output_path)


