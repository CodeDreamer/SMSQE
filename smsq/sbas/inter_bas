REM program to list interpreter tokens

open_over #4,'win1_smsq_sbas_inter_codes'
width #4,100

open_in #3,'win1_smsq_sbas_inter_asm'

rep xx
        input #3,a$
        if a$='sbi_table': exit xx
endrep xx

maxc = 399
dim v$(maxc,12)
t$=chr$(9)

for i = 7 to maxc
  rep xx
    input #3,a$
    rep ntab
      tp = t$ instr a$
      if tp: a$(tp) = ' ': else exit ntab
    endrep ntab
    rep sspce
      ts = '  ' instr a$
      if ts: a$ = a$(1 to ts) & a$(ts+2 to): else exit sspce
    endrep sspce
    if a$=' end': exit i
    if ' vector ' instr a$ = 1: exit xx
  end rep xx
  n = ',' instr a$
  v$(i) = a$(9 to n+1)
endfor i

maxc = i - 1

for i = 0 to maxc step 8
  print #4; hex$ (i*2,12);
  for o=0 to 3: print #4; to o*11+6; v$(i+o);
  print #4; to 4*11+6; hex$ (i*2+8,12);
  for o=4 to 7: print #4; to o*11+11; v$(i+o);
  print #4
endfor i
  
