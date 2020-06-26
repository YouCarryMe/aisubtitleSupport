import srt
import re

from os import sys

class Tag:
    B_START, B_END = '<b>', '</b>'  # 粗体
    I_START, I_END = '<i>', '</i>'  # 斜体
    U_START, U_END = '<u>', '</u>'  # 下划线
    COLOR_START, COLOR_END = '<font color = {}>', '</font>'
    # 字幕支持的颜色，可自行增减
    colors = {'red': '#FF0000', 'white': '#FFFFFF', 'yellow': '#FFFF00', 'blue': '#0000FF', 'black': '#000000'}

    def __init__(self):
        pass

    def delete_b_tag(self, content):
        return content.replace(self.B_START, '').replace(self.B_END, '')

    def add_b_tag(self, content):
        content = self.delete_b_tag(content)
        return self.B_START + content + self.B_END

    def delete_i_tag(self, content):
        return content.replace(self.I_START, '').replace(self.I_END, '')

    def add_i_tag(self, content):
        content = self.delete_i_tag(content)
        return self.I_START + content + self.I_END

    def delete_u_tag(self, content):
        return content.replace(self.U_START, '').replace(self.U_END, '')

    def add_u_tag(self, content):
        content = self.delete_u_tag(content)
        return self.U_START + content + self.U_END

    def delete_color_tag(self, content):
        ls_colors = list(self.colors.values())
        for color in ls_colors:
            content = content.replace(self.COLOR_START.format(color), '')
        return content.replace(self.COLOR_END, '')

    def add_color_tag(self, content, color_name):
        color = self.colors[color_name]
        content = self.delete_color_tag(content)
        return self.COLOR_START.format(color) + content + self.COLOR_END

    @staticmethod
    def delete_all_tags(content):
        content = re.sub(r'<\/?[\s\S]*?(?:".*")*>', '', content)  # 去除所有<>标签
        return content

    @staticmethod
    def delete_all_tags_subs(subs, length):
        for i in range(length):
            subs[i].content = Tag.delete_all_tags(subs[i].content)
        return subs

    def add_b_tags(self, subs, length):
        for i in range(length):
            subs[i].content = self.add_b_tag(subs[i].content)
        return subs

    def add_i_tags(self, subs, length):
        for i in range(length):
            subs[i].content = self.add_i_tag(subs[i].content)
        return subs

    def add_u_tags(self, subs, length):
        for i in range(length):
            subs[i].content = self.add_u_tag(subs[i].content)
        return subs

    def add_color_tags(self, subs, length, color_name):
        for i in range(length):
            subs[i].content = self.add_color_tag(subs[i].content, color_name)
        return subs


def main(file_path, new_file_path, select_all, tag, index, start_time, end_time, content):
    """
    当select_all == 'True'时，后四个参数传入空字符串即可。
    当select_all == 'False'时，使用content更新字幕内容。若此时指定了tag，tag则作用于content上，然后更新字幕内容。

    :param file_path:       原始字幕文件路径
    :param new_file_path:   新字幕文件路径
    :param select_all:      是否选择全体字幕    'True' or 'False'
    :param tag:             为字幕添加的标签    '' or 'b' or 'i' or 'u' or 'delete' or 'red' or 'yellow' ...
    :param index:           单个字幕的索引     '1' or '2' ...
    :param start_time:      单个字幕的起始时间   'HH:MM:MM.XXX'
    :param end_time:        必须大于起始时间    'HH:MM:MM.XXX'
    :param content:         单个字幕的新内容
    :return:
    """

    # 从file_path读取字幕
    with open(file_path, mode='r', encoding='utf-8') as f:
        srt_s = f.read()
    subs = list(srt.parse(srt_s))
    length = len(subs)

    select_all = eval(select_all)
    my_tag = Tag()
    if select_all:
        if tag == 'b':
            subs = my_tag.add_b_tags(subs, length)
        elif tag == 'u':
            subs = my_tag.add_u_tags(subs, length)
        elif tag == 'i':
            subs = my_tag.add_i_tags(subs, length)
        elif tag == 'delete':
            subs = Tag.delete_all_tags_subs(subs, length)
        elif tag in list(my_tag.colors.keys()):
            subs = my_tag.add_color_tags(subs, length, tag)
    else:
        index = eval(index) - 1  # 或许要加一层遍历
        start_time = start_time.replace('.', ',')
        end_time = end_time.replace('.', ',')
        subs[index].start = srt.srt_timestamp_to_timedelta(start_time)
        subs[index].end = srt.srt_timestamp_to_timedelta(end_time)

        if tag == 'b':
            content = my_tag.add_b_tag(content)
        elif tag == 'u':
            content = my_tag.add_u_tag(content)
        elif tag == 'i':
            content = my_tag.add_i_tag(content)
        elif tag == 'delete':
            subs = Tag.delete_all_tags(content)
        elif tag in list(my_tag.colors.keys()):
            content = my_tag.add_color_tag(content, tag)

        subs[index].content = content

    # 向new_file_path写入字幕
    new_srt_s = srt.compose(subs)
    with open(new_file_path, mode='w+', encoding='utf-8') as f:
        f.write(new_srt_s)


if __name__ == '__main__':
    # main('123_zh.srt', '123_zh_test.srt', 'True', 'red', '', '', '', '')
    # main('123_zh.srt', '123_zh_test.srt', 'True', 'delete', '', '', '', '')
    # main('test_zh.srt', '123_zh_test.srt', 'False', '', '1', '00:01:42.430', '00:01:45.090', '归去来兮')
    pass
