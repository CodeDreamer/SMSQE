* Queue maintenance: put byte into a queue   V2.00    Tony Tebby  QJUMP
*
        section ioq
*
        xdef    ioq_pbyt
*
        xref    ioq_nc
        xref    ioq_eof
*
        include dev8_keys_qu
*
*       d0  r   error condition (0, eof or not complete)
*       d1 c  p next byte in queue (if d0=0)
*       a2 c  p pointer to queue 
*       a3   sp
*
*       all other registers preserved
*
*
ioq_pbyt
        move.l  a3,-(sp)                save scratch
        tst.b   qu_eoff(a2)             end of file?
        bmi.s   ioq_eof                 ... yes, set it
*
        move.l  qu_nexti(a2),a3         next in
        move.b  d1,(a3)+                set byte
        cmp.l   qu_endq(a2),a3          next off end?
        bne.s   iqp_seti                ... no
        lea     qu_strtq(a2),a3         ... yes, reset queue pointer
iqp_seti
        cmp.l   qu_nexto(a2),a3         is there room?
        beq.s   ioq_nc                  ... no, set not complete
        move.l  a3,qu_nexti(a2)         set next in
        move.l  (sp)+,a3                restore scratch register
        moveq   #0,d0                   ok
        rts
        end
