; test date conversion long to date/time  string  QL compatible    1990  Tony Tebby

        section test

        xref    uq_ldate
        xref    uq_ldowk

        xref    cv_decil
        xref    gu_fopen
        xref    gu_clra
        xref    gu_flin
        xref    gu_sstrg
        xref    gu_nl
        xref    gu_iowp
        xref    gu_die

        include 'dev8_keys_qlv'
        include 'dev8_keys_qdos_ioa'
        include 'dev8_keys_qdos_io'

uqt_dchn equ    $00
uqt_date equ    $04
uqt_buff equ    $10
uqt.buff equ      $40
uqt_buft equ    uqt_buff+uqt.buff

        data    $400

        bra.s   start
        dc.w    0,0,$4afb
        dc.w    9,'Test DATE'

con     dc.w    17,'CON_364x102a74x15'

start
        lea     (a6,a4.l),a5             ; data space

        sub.l   a6,a6

        lea     con,a0
        moveq   #ioa.kexc,d3
        jsr     gu_fopen                 ; open console
        move.l  a0,uqt_dchn(a5)          ; and our input channel as well

        moveq   #7,d1                    ; add a border
        moveq   #1,d2
        moveq   #iow.defb,d0
        jsr     gu_iowp

        jsr     gu_clra                  ; and clear all

uqt_loop
        move.w  #uqt.buff,d1             ; buffer length
        lea     uqt_buff(a5),a1
        jsr     gu_flin                  ; fetch line
        bne.s   uqt_die
        cmp.b   #'*',(a1)
        beq.s   uqt_done                 ; done

        move.l  a1,a0
        add.w   d1,a1
        move.l  a1,d7                    ; buffer pointers
        jsr     cv_decil
        move.l  d1,uqt_date(a5)          ; keep date

        move.l  uqt_dchn(a5),a0
        lea     uqt_buft(a5),a1
        move.w  cv.ilday,a2
        jsr     (a2)
        move.w  #'  ',(a1)
        move.w  cv.ildat,a2
        jsr     (a2)
        move.w  #25,(a1)
        jsr     gu_sstrg
        jsr     gu_nl

        trap    #15

        lea     uqt_buft(a5),a1
        jsr     uq_ldowk
        move.w  #'  ',(a1)
        jsr     uq_ldate
        move.w  #25,(a1)
        jsr     gu_sstrg
        jsr     gu_nl
        bra.s   uqt_loop

uqt_done
        moveq   #0,d0                    ; done
uqt_die
        jmp     gu_die

        end
