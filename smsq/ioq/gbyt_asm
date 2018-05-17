* Queue maintenance: get byte from a queue   V2.00    Tony Tebby  QJUMP
*
        section ioq
*
        xdef    ioq_gbyt
*
        xref    ioq_empty
*
        include dev8_keys_qu
*
*       d0  r   error condition (0, eof or not complete)
*       d1  r   next byte in queue (if d0=0)
*       a2 c  p pointer to queue 
*       a3   sp
*
*       all other registers preserved
*
*
ioq_gbyt
        move.l  a3,-(sp)                save scratch
        move.l  qu_nexto(a2),a3         next out
        cmp.l   qu_nexti(a2),a3         any thing there?
        beq.s   ioq_empty               ... no
        move.b  (a3)+,d1                set next character
        cmp.l   qu_endq(a2),a3          next off end?
        bne.s   iqg_ok                  ... no
        lea     qu_strtq(a2),a3         ... yes, reset queue pointer
iqg_ok
        move.l  a3,qu_nexto(a2)         set next out
        move.l  (sp)+,a3                restore scratch register
        moveq   #0,d0                   ok
        rts
        end
