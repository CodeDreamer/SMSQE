open #1,'con': cls: border 1,4
a=alchp (65536)
for i=a to a+65535 step 2: poke_w i,-1
lbytes 'win1_dd_gold_driver',a
lbytes 'win1_sys_gold_patch',a+32768
lbytes 'win1_sys_gold_boot',a+32768+16384
chk=510
print 'Working ....'
for i=a to a+65531:chk=chk+peek(i)
m=int(chk/65536+.5)
chk%=chk-m*65536
poke_w a+65532,~~chk%: poke_w a+65534,chk%
print '             .... saving'
sbytes_o 'flp1_gold',a,65536
