open #1,'con_322x56a94x20': cls: border 1,4
paper 4: cursor 0,24: cls 1: ink 0:
cursor 114,1: print 'NETWORK TIMINGS'
cursor 0,12: print 'Press Capital to increase figure, miniscule to reduce'

dim val(3): dim loc(3)
key$='WDR': loc(1)=102: loc(2)=108: loc(3)=110
link=hex('28048')
rep look_link
  link=peek_l(link)
  if link=0: cursor 100,34: input 'Net not found ';a$: stop
  if peek_w(link+60-24)<>1: next look_link
  if peek(link+62-24)=code('N'): link=link-24: exit look_link
endrep look_link

paper 0: ink 7
cursor 60,24: under 1: print 'W';:under 0:print 'rite loop timing constant ....'
cursor 60,34: under 1: print 'D';:under 0:print 'elay to read loop ............'
cursor 60,44: under 1: print 'R';:under 0:print 'ead loop timing constant .....'
ink 7

for v=1 to 3: val(v)=peek_w(link+loc(v)): write_val v

rep keyin
  a$=inkey$(#1,-1)
  v= a$ instr key$
  if v
    if a$=key$(v): val(v)=val(v)+1: else val(v)=val(v)-1
    if val(v)<0: val(v)=0
    write_val v
  else
    if code(a$)=27: stop
  endif
endrep keyin

def proc write_val (v)
  cursor 240,14+10*v: print_using '###',val(v)
  poke_w link+loc(v),val(v)
enddef
