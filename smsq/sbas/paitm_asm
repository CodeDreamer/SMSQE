; SBAS_PAITM - SuperBASIC Parser Items          1992 Tony Tebby

        section sbas

        xdef    sb_patxt     ; parse text item
        xdef    sb_pastr     ; parse string item
        xdef    sb_pakey     ; parse keywords
        xdef    sb_panky     ; parse non-keywords

        xref    sb_parcl

        xref    sbp_dkey
        xref    sbp_skey
        xref    sbp_nkey
        xref    sbp_intb

        include 'dev8_keys_sbasic'
        include 'dev8_keys_err'
        include 'dev8_smsq_sbas_parser_keys'
        include 'dev8_keys_k'
        include 'dev8_mac_assert'

;+++
; Check string (text)
;---
sb_pastr
        addq.l  #1,a0
        move.l  a0,a1                    ; save start of text
sbp_gstr
        move.b  (a6,a0.l),d0
        cmp.b   d1,d0                    ; delimiter
        beq.s   sbp_stxt
        addq.l  #1,a0
        cmp.b   #k.nl,d0                 ; end of line?
        bne.s   sbp_gstr

        moveq   #sbe.bstr,d0             ; bad string
        bra.s   sbp_rts

sbp_stxt
        bsr.s   sbp_stext
        addq.l  #1,a0                    ; skip delimiter
        rts

;+++
; Get text item
;---
sb_patxt
        move.l  a0,a1                    ; text goes from here
        move.l  sb_buffp(a6),a0          ; ... to here
        subq.l  #1,a0                    ; ... nearly
        move.w  #tkw.text,d1
        moveq   #tki.text,d2

sbp_stext
        move.w  d1,(a6,a4.l)             ; string/text token
        move.l  a0,d1
        sub.l   a1,d1                    ; length of string
        move.w  d1,2(a6,a4.l)
        addq.l  #4,a4
        sub.l   a0,a1                    ; buffer may move, only A0/A4 are adjusted
        jsr     sb_parcl                 ; reserve command line space
        add.l   a0,a1
        bra.s   sbp_ctxe

sbp_ctext
        move.b  (a6,a1.l),(a6,a4.l)      ; copy byte
        addq.l  #1,a1
        addq.l  #1,a4
sbp_ctxe
        cmp.l   a0,a1                    ; more to copy?
        blt.s   sbp_ctext

        move.l  a4,d0
        and.w   #1,d0
        add.w   d0,a4                    ; round up

        moveq   #0,d0                    ; always OK
sbp_rts
        rts

;+++
; Check keywords
;---
sb_pakey
sbk.reg reg     a1/a2
sbk_item equ    $00

        movem.l sbk.reg,-(sp)
        moveq   #$ffffffdf,d3           ; upper / lower case diff

; start off with the doubles

        lea     sbp_dkey,a3              ; table of double keywords

sbk_dloop
        assert  sbg_ditm,sbg_dabb-1,0
        move.w  (a3)+,d5                 ; token / abbreviation
        beq.s   sbk_sing                 ; ... no more doubles
        cmp.b   d5,d2                    ; long enough?
        blt.s   sbk_nxtd

        lea     sbg_nam1-2(a3),a2
        add.w   (a2),a2                  ; first name
        addq.w  #2,a2                    ; skip length
        move.w  d2,d0                    ; length to check

        subq.w  #1,d0
sbk_ck1
        move.b  (a6,a1.l),d1             ; next to check
        addq.l  #1,a1
        move.b  (a2)+,d5                 ; against this
        eor.b   d5,d1
        and.b   d3,d1
        dbne    d0,sbk_ck1

        addq.w  #1,d0
        move.w  d2,d1
        sub.w   d0,d1                    ; actual number of chars which match

        cmp.b   sbg_abb1-2(a3),d1        ; enough chars for a match?
        blt.s   sbk_nxtd                 ; ... no
        cmp.b   sbg_abb2-2(a3),d0        ; enough chars in second bit?
        blt.s   sbk_nxtd                 ; ... no

        lea     sbg_nam2-2(a3),a2
        add.w   (a2),a2                  ; second name
        addq.w  #2,a2                    ; skip length

        subq.w  #1,d0
sbk_ck2
        move.b  -1(a6,a1.l),d1             ; next to check
        addq.l  #1,a1
        move.b  (a2)+,d5                 ; against this
        eor.b   d5,d1
        and.b   d3,d1
        dbne    d0,sbk_ck2

        beq.s   sbk_setd                 ; match set double

sbk_nxtd
        move.l  sbk_item(sp),a1          ; restore item pointer
        addq.l  #sbg.ditm-2,a3           ; next double item
        bra.s   sbk_dloop

sbk_setd
        move.b  sbg_ditm-2(a3),d2        ; item
        moveq   #0,d0
        assert  sbg_itm1,sbg_itm2-1
        move.w  sbg_itm1-2(a3),d1
        move.b  d1,d0
        lsr.w   #7,d1                    ; first item
        add.b   d0,d0                    ; second item
        lea     sbp_intb,a2
        move.w  (a2,d1.w),(a6,a4.l)      ; set tokens
        move.w  (a2,d0.w),2(a6,a4.l)
        addq.l  #4,a4
        bra.s   sbk_exok

; now try the singles

sbk_sing
        lea     sbp_skey,a3              ; table of single keywords

sbk_sloop
        move.l  sbk_item(sp),a1          ; restore item pointer
        assert  sbg_item,sbg_iabb-1,0
        move.w  (a3)+,d5                 ; token / abbreviation
        beq.s   sbk_nf                   ; ... no more singles
        assert  sbg_name,2
        move.l  a3,a2
        add.w   (a3)+,a2                 ; pointer to name
        cmp.b   d5,d2                    ; long enough?
        blt.s   sbk_sloop
        cmp.w   (a2)+,d2                 ; too long?
        bgt.s   sbk_sloop

        move.w  d2,d0

        subq.w  #1,d0
sbk_ckc
        move.b  (a6,a1.l),d1             ; next to check
        addq.l  #1,a1
        move.b  (a2)+,d5                 ; against this
        eor.b   d5,d1
        and.b   d3,d1
        dbne    d0,sbk_ckc

        bne.s   sbk_sloop                ; no match, try again

sbk_seti
        move.b  sbg_item-sbg.item(a3),d2 ; item
        moveq   #0,d0
        move.b  d2,d0
        add.b   d0,d0                    ; item index
        lea     sbp_intb,a2
        move.w  (a2,d0.w),(a6,a4.l)      ; set token
        addq.l  #2,a4

sbk_exok
        moveq   #0,d0
sbk_exit
        movem.l (sp)+,sbk.reg
        rts

sbk_nf
        moveq   #err.itnf,d0
        bra.s   sbk_exit

;+++
; Check non-keyword items
;---
sb_panky
        movem.l sbk.reg,-(sp)

        lea     sbp_nkey,a3              ; table of non keywords

sbk_nloop
        assert  sbg_item,sbg_iabb-1,0
        move.w  (a3)+,d5                 ; token / abbreviation = length
        beq.s   sbk_nf                   ; ... no more non-keywords
        assert  sbg_name,2
        move.l  a3,a2
        add.w   (a3)+,a2                 ; pointer to characters
        move.w  (a2)+,d0
        cmp.b   (a2)+,d1
        bne.s   sbk_nloop                ; first char does not match

        lea     1(a0),a1                 ; set next char pointer
        subq.w  #2,d0
        blt.s   sbk_seta0                ; only one char, done

sbk_ckn
        move.b  (a6,a1.l),d2             ; next to check
        addq.l  #1,a1
        sub.b   (a2)+,d2                 ; against this
        dbne    d0,sbk_ckn

        bne.s   sbk_nloop                ; no match, try again

sbk_seta0
        move.l  a1,a0                    ; update buffer pointer
        bra.s   sbk_seti                 ; set item

        end
