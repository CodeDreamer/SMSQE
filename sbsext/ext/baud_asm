; BAUD         V2.00      1990  Tony Tebby   QJUMP
;
;       BAUD rate
;
        section exten

        xdef    baud

        xref    ut_gtlin                 ; get long integers

        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_err'

baud
        jsr     ut_gtlin                 ; get rate / port nr
        bne.s   baud_rts
        moveq   #0,d1                    ; assume no port
        cmp.w   #1,d3                    ; 0, 1, 2
        blt.s   baud_bad
        beq.s   baud_set
        move.l  4(a6,a1.l),d1
baud_set
        add.l   (a6,a1.l),d1
        moveq   #sms.comm,d0
        trap    #do.sms2
baud_rts
        rts

baud_bad
        moveq   #err.ipar,d0
        rts
        end
