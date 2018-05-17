; DMP send buffer       V2.01     1987  Tony Tebby  QJUMP

        section sdp

        xdef    sdp_sbuf

        xref    sdp_save

        include dev8_keys_err
        include dev8_keys_qdos_io
        include dev8_sys_sdp_data

reglist reg     d1/d2/d3/a0/a1           ; trap #3 registers

sdp_sbuf
        movem.l reglist,-(sp)            ; save volatiles
        moveq   #0,d0                    ; assume OK
        move.w  dp_bufpt(a5),d2          ; any to dump?
        beq.s   ds_exit                  ; ... no

        moveq   #iob.smul,d0             ; send multiple bytes
        moveq   #forever,d3              ; waiting forever
        tst.l   dp_pstk(a5)              ; supervisor mode?
        beq.s   ds_chan
        moveq   #no.wait,d3              ; no wait
ds_chan
        move.l  dp_chan(a5),a0           ; to channel
        lea     dp_buffr(a5),a1          ; from buffer
        trap    #3
        sub.w   d1,dp_bufpt(a5)          ; number left in buffer
        tst.w   d3                       ; no wait?
        bne.s   ds_test                  ; ... no

        cmp.l   #err.nc,d0               ; not complete?
        blt.s   ds_test                  ; ... no, error
        bgt.s   ds_save                  ; ... done, save

        lea     dp_buffr(a5),a0          ; start of buffer
ds_copy
        move.b  (a1)+,(a0)+              ; copy one byte down
        addq.w  #1,d1                    ; one gone
        cmp.w   d1,d2                    ; all gone?
        bgt.s   ds_copy                  ; ... no

ds_save
        movem.l (sp)+,reglist            ; restore regs
        jsr     sdp_save                 ; save status
        bra.s   sdp_sbuf                 ; and retry!!!

ds_test
        tst.l   d0                       ; test error
ds_exit
        movem.l (sp)+,reglist            ; restore volatiles
        rts
        end
