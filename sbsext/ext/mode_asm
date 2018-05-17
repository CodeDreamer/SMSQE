; MODE         V2.00      1990  Tony Tebby   QJUMP
;
;       MODE 0/4/8/256/512
;
        section exten

        xdef    mode

        xref    ut_gxin1                 ; get one integer

        include 'dev8_keys_qdos_sms'

mode
        jsr     ut_gxin1                 ; get mode
        bne.s   mode_rts
        move.w  (a6,a1.l),d1
        move.w  d1,d0
        lsr.w   #5,d0                    ; allow 256 = mode 8
        or.b    d0,d1
        and.w   #8,d1
        moveq   #-1,d2                   ; no change to TV
        moveq   #sms.dmod,d0
        trap    #do.sms2
mode_rts
        rts
        end
