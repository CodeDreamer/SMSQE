* Cursor control      V0.9    Tony Tebby   QJUMP
*
*       cursen [#n]                     enables the cursor
*       curdis [#n]                     disables the cursor
*
        section exten
*
        xdef    cursen
        xdef    curdis
*
        xref    ut_par0                 check for no parameters
        xref    ut_chan1                get channel default #1
*
        include dev8_sbsext_ext_keys
*
*
* enable the cursor
*
cursen
        moveq   #sd.cure,d5     set cursor enable key
        bra.s   cur_com
*
* disable the cursor
*
curdis
        moveq   #sd.curs,d5     set cursor suppression key     
cur_com
        bsr.l   ut_chan1        use common routine for channel id in a0
        bne.s   cur_exit        ... ok?
        bsr.l   ut_par0
        move.b  d5,d0           set key in d0
        moveq   #-1,d3          do not timeout
        trap    #3              do io trap (sets d0 to error code)
cur_exit
        rts
        end
