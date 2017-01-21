def gen_bpm():
    file = open('bpm.song', 'w')
    seconds = 3000 * 1000

    sum = 0
    while sum < seconds:
        sum += 324
        file.write(str(sum)+'/0-1\n')

    file.close()

def parse_chart():
    file = open('blitzkrieg.chart', 'r')
