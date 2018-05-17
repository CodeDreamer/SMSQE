; Procedure to List hotkeys    1990   Tony Tebby

        section hotkey

        xdef    hxt_list

        xref    hxt_char

        xref    thp_chn

        xref    hk_fitmc
        xref    hk_getpr

        xref    xu_wset
        xref    xu_wline
        xref    gu_hkuse
        xref    gu_hkfre
        xref    cv_vchar
        xref    cv_vchrs
        xref    cv_hktyp

        xref    gu_achp0
        xref    gu_rchp

        xref    met_spce

        include 'dev8_keys_qdos_io'
        include 'dev8_keys_k'
        include 'dev8_ee_hk_data'
        include 'dev8_sys_xut_data'
        include 'dev8_keys_thg'
        include 'dev8_mac_thg'

hls.type equ    9       position of Type
hls.text equ    14      position of name
;+++ 
; List the Hotkeys
;
; HOT_LIST channel
;---
hxt_list thg_extn LIST,hxt_char,thp_chn
hls.reg reg     d1/d2/d3/d4/d5/d6/d7/a0/a1/a2/a3/a4/a5/a6
        movem.l hls.reg,-(sp)

        lea     -$70(sp),a5              ; WUT data block
        clr.l   -(a5)
        clr.l   -(a5)
        clr.l   -(a5)
        clr.l   -(a5)

        move.w  #2048,d7                 ; our largest hotkey!!
        moveq   #20,d0                   ; safety barrier
        add.w   d7,d0

        jsr     gu_achp0
        bne.l   hls_exit

        move.l  (a1),xu_dchan(a5)        ; channel ID
        jsr     xu_wset                  ; get channel ID and set
        moveq   #0,d6
        move.w  xu_cols(a5),d6           ; width of each line
        sub.w   d6,d7                    ; remaining working space
        add.w   d7,d6
        add.l   a0,d6                    ; top of line buffer
        addq.l  #2,d6                    ; string length

hls_gtall
        move.w  #$feff,d4                ; hotkey

hls_nloop
        jsr     gu_hkuse                 ; find hotkey linkage
        bne.l   hls_rchp

hls_retry
        addq.w  #1,d4
        bge.l   hls_frch                 ; done, free and return heap

        moveq   #0,d1
        move.b  d4,d1
        jsr     hk_fitmc                 ; find item with given case
        bne.s   hls_retry                ; ... none

; now the tricky get name bit

        cmp.w   #hki.stbf,hki_type(a1)   ; stuff buffer?
        beq.s   hls_stbf                 ; ... yes, find it
        cmp.w   #hki.stpr,hki_type(a1)   ; stuff previous?
        beq.s   hls_stpr                 ; ... yes

        lea     hki_name(a1),a2
        move.l  a0,a6                    ; fill our buffer
        move.w  (a2)+,d1                 ; length
        cmp.w   d7,d1
        bls.s   hls_nle
        move.w  d7,d1
        bra.s   hls_nle
hls_nlp
        move.b  (a2)+,(a6)+
hls_nle
        dbra    d1,hls_nlp
        bra.s   hls_key

hls_stpr
        move.l  hkd_bpnt(a3),hkd_ppnt(a3) ; current
        jsr     hk_getpr                 ; get previous
        move.l  hkd_ppnt(a3),a2
        move.l  hkd_bpnt(a3),hkd_ppnt(a3) ; and reset pointer
        bra.s   hls_pstbf
hls_stbf
        move.l  hkd_bpnt(a3),a2          ; point to stuff string
hls_pstbf
        move.l  a0,a6
        move.w  d7,d0
hls_fend
        move.b  (a2)+,(a6)+              ; end?
        dbeq    d0,hls_fend

        subq.l  #1,a6                    ; end

hls_key
        move.l  a1,a4
        move.w  d4,d1                    ; write out character
        lea     met_spce,a2
        lea     (a0,d7.w),a1             ; start of buffer
        jsr     cv_vchrs
        moveq   #hls.type,d3
        move.w  (a1)+,d0
        move.l  a1,d2                    ; start of chars
        add.w   d0,a1

        sub.w   d0,d3                    ; we need this many spaces
hls_tab1
        move.b  #' ',(a1)+
        subq.w  #1,d3
        bgt.s   hls_tab1

        move.w  hki_type(a4),d1          ; get type
        asr.w   #1,d1                    ; ... ignoring transient flag
        asl.w   #2,d1

        lea     cv_hktyp,a2              ; type table
        add.w   d1,a2
        move.b  (a2)+,(a1)+
        move.b  (a2)+,(a1)+
        move.b  (a2)+,(a1)+
        move.b  (a2)+,(a1)+
        move.b  #' ',(a1)+

        jsr     gu_hkfre                 ; free hotkey

        move.l  a0,a2                    ; fill output buffer from ws
hls_lloop
        moveq   #0,d3
hls_cloop
        cmp.l   a6,a2                    ; at end
        bge.s   hls_eline
        move.b  (a2)+,d1
        cmp.b   #k.enter,d1              ; enter?
        beq.s   hls_newl

        move.l  a1,a3                    ; save pointer
        lea     $10(a5),a1
        jsr     cv_vchar                 ; make visible
        exg     a1,a3
        beq.s   hls_stnd                 ; standard

        moveq   #0,d0
        move.w  (a3)+,d0
        add.l   a1,d0                    ; new end?
        cmp.l   d6,a0                    ; off end?
        bgt.s   hls_back                 ; ... yes back
hls_nslp
        move.b  (a3)+,(a1)+
        cmp.l   d0,a1                    ; copy ns charecters
        blt.s   hls_nslp

        cmp.l   d6,a1                    ; new position
        bge.s   hls_newl                 ; ... just at end
        tas     d3                       ; was previous a normal character?
        bne.s   hls_cloop                ; ... no
        move.b  #' ',(a1)+
        bra.s   hls_eol

hls_back
        subq.l  #1,a2                    ; backspace the pointers
        bra.s   hls_newl

hls_stnd
        move.b  2(a3),(a1)+
        sf      d3
hls_eol
        cmp.l   d6,a1                    ; end of buffer?
        blt.s   hls_cloop

hls_newl
        bsr.s   hls_wline                ; newline
        clr.w   (a1)+
        move.l  a1,d2                    ; save start
        moveq   #hls.text-1,d0
hls_cllp
        move.b  #' ',(a1)+               ; and tab in
        dbra    d0,hls_cllp
        bra.s   hls_lloop

hls_eline
        cmp.l   a0,a2                    ; any text at all?
        beq.s   hls_fline                ; ... yes, flush
        lea     hls.text+2(a0,d7.w),a3   ; start of text
        cmp.l   a3,a1                    ; any on this line?
        ble     hls_nloop                ; ... no

hls_fline
        bsr.s   hls_wline
        bra     hls_nloop

hls_frch
        moveq   #0,d0                    ; ok
        jsr     gu_hkfre                 ; free hk
hls_rchp
        jsr     gu_rchp                  ; return heap
hls_exit
        movem.l (sp)+,hls.reg
        rts

hls_wline
        exg     d2,a1
        sub.l   a1,d2
        move.w  d2,-(a1)                 ; make it a string
        jmp     xu_wline                 ; send line


        end
