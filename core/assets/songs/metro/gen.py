def gen_bpm():
    file = open('metro.song', 'w')
    seconds = 3000 * 1000

    sum = 0
    from random import randint
    while sum < seconds:
        sum += 1000
        me = randint(0, 3)
        file.write(str(sum)+'/0-' + str(me) + '\n')

    file.close()

def parse_chart(filename):
    from random import randint
    found_dif = False
    with open(filename + '.chart', 'r') as f:
        content = f.readlines()

        lineStartMusic = 0
        lineEndMusic = 0
        for i in xrange(0, len(content)):
            if "ExpertSingle" in content[i]:
                found_dif = True
                lineStartMusic = i + 2
            if "}" in content[i] and found_dif:
                lineEndMusic = i - 1
                break

        out = open(filename + '.song', 'w')
        for i in xrange(lineStartMusic, lineEndMusic):
            line = content[i].strip()
            splits = line.split(" = ")
            time = splits[0]
            chord_info = splits[1].split(" ")
            chord = chord_info[1]
            if chord == "4": continue
            if randint(0, 4) > 1: continue

            duration = chord_info[2]
            out.write(time+"/"+duration+"-"+chord+"\n")

gen_bpm()

