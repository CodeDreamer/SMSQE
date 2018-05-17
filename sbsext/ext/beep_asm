; BEEP / BEEPING         V2.00      1990  Tony Tebby   QJUMP
;
;       BEEP  duration, pitch etc
;       BEEPING
;
        section exten

        xdef    beep
        xdef    beeping

        xref    ut_gtint                 ; get integers
        xref    ut_retin
        xref    ut_par0

        include 'dev8_keys_sbasic'
        include 'dev8_keys_sys'
        include 'dev8_keys_qdos_sms'

beep
        jsr     ut_gtint                 ; get parameters
        bne.s   beep_rts

        move.l  sb_buffb(a6),a3          ; parameter block
        add.l   a6,a3
        move.l  a3,a0                    ; pointer to fill

        subq.b  #1,d3                    ; how many?
        blt.s   beep_stop

        clr.l   (a3)+                    ; zero values for missing
        clr.l   (a3)+
        clr.l   (a3)+
        clr.l   (a3)+

        add.l   a6,a1
        move.l  a0,a2

beep_cprm
        move.w  (a1)+,(a0)+              ; copy parameters given
        dbra    d3,beep_cprm

        move.l  a3,a0                    ; and set command

        move.w  #$0a0a,(a0)+             ; beep
        move.l  #%00000000101010101010,(a0)+
        move.b  3(a2),(a0)
        addq.b  #1,(a0)+                 ; pitch 1
        move.b  5(a2),(a0)
        addq.b  #1,(a0)+                 ; pitch 2
        move.b  7(a2),(a0)+              ; interval
        move.b  6(a2),(a0)+
        move.b  1(a2),(a0)+              ; duration
        move.b  0(a2),(a0)+
        move.b  9(a2),(a0)+              ; step
        move.b  $b(a2),(a0)+             ; wrap
        move.b  $d(a2),(a0)+             ; rand
        move.b  $f(a2),(a0)+             ; fuzz

        bra.s   beep_hdop

beep_stop
        move.w  #$0b00,(a0)+             ; beep off
        clr.l   (a0)+                    ; no params

beep_hdop
        move.b  #%01,(a0)+               ; no reply

        moveq   #sms.hdop,d0
        trap    #do.sms2

beep_rts
        rts

beeping
        jsr     ut_par0                  ; no parameters please
        moveq   #sms.info,d0
        trap    #do.sms2
        moveq   #1,d1
        and.b   sys_qlbp(a0),d1          ; beep status from sysvars
        move.l  sb_arthp(a6),a1
        subq.l  #2,a1
        move.w  d1,(a6,a1.l)             ; on stack
        jmp     ut_retin


        end
