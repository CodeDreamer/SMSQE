; Make executable item      V2.00     1990   Tony Tebby   QJUMP

        section exten

        xdef    hxt_mkxi
        xdef    hxt_mkxt

        xref    gu_achp0
        xref    gu_achpp

        include 'dev8_keys_thg'
        include 'dev8_ee_hk_data'
        include 'dev8_ee_hk_xhdr'
        include 'dev8_ee_hxt_data'

;+++
; Make executable item with name (permanent memory allocation)
;
;       d6 c  p (word) item type
;       a0  r   base of item
;       a1 c  p pointer to standard parameters
;       a4 c  p pointer to item name
;---
hxt_mkxi
hmx.reg reg     d1/a2/a4
        movem.l hmx.reg,-(sp)
        lea     gu_achpp,a0
        bra.s   hmx_job

;+++
; Make executable item (temporary memory allocation)
;
;       d6 c  p (word) item type
;       a0  r   base of item
;       a1 c  p pointer to standard parameters
;---
hxt_mkxt
        movem.l hmx.reg,-(sp)
        lea     gu_achp0,a0
        move.l  hxt_itnm(a1),a4          ; item name

hmx_job
        move.l  hxt_jbnm(a1),d1          ; job name?
        bne.s   hmx_nspc                 ; ... yes, allocate space
        move.l  hxt_wknm(a1),d1          ; wake name?
        beq.s   hmx_pspc                 ; ... no, just allocate param space
hmx_nspc
        move.l  d1,a2                    ; pointer to string
        moveq   #1,d1                    ; room for separator
        add.w   (a2),d1

hmx_pspc
        move.l  hxt_parm(a1),d0          ; parameters?
        beq.s   hmx_ispc                 ; ... no
        move.l  d0,a2
        addq.w  #3,d1                    ; room for separators / delimiters
        add.w   (a2),d1                  ; and parameters

hmx_ispc
        add.w   (a4),d1                  ; item name space

        moveq   #hki_name+2+3,d0         ; allocate heap item + JMP (A6)
        add.w   d1,d0

        jsr     (a0)                     ; allocate
        bne.s   hmx_exit

        move.w  #hki.id,(a0)+            ; set id
        move.w  d6,(a0)+                 ; type of action
        clr.l   (a0)+                    ; pointer not set
        clr.w   (a0)+                    ; no name

        move.l  a0,d1                    ; start of name characters

        move.l  a4,a2
        bsr.s   hmx_cnam                 ; copy item name

        move.l  hxt_parm(a1),d0          ; parameters?
        beq.s   hmx_jbnm
        move.b  #hki.psep,(a0)+          ; ... yes
        move.b  #hki.popn,(a0)+
        bsr.s   hmx_cnd0
        move.b  #hki.pcls,(a0)+

hmx_jbnm
        move.l  hxt_jbnm(a1),d0          ; job name?
        beq.s   hmx_wknm                 ; ... no
        move.b  #hki.jsep,(a0)+
        bra.s   hmx_name
hmx_wknm
        move.l  hxt_wknm(a1),d0          ; wake name?
        beq.s   hmx_done
        move.b  #hki.wsep,(a0)+
hmx_name
        bsr.s   hmx_cnd0

hmx_done
        move.l  a0,d0                    ; round up
        addq.l  #1,d0
        bclr    #0,d0
        move.l  d0,a2                    ; where to put pre-process code
        move.w  #hkh.jpa6,(a2)           ; ... not a lot

        exg     a0,d1                    ; where we have got to
        sub.l   a0,d1
        move.w  d1,-(a0)                 ; name length
        subq.l  #hki_name,a0             ; item base
        moveq   #0,d0

hmx_exit
        movem.l (sp)+,hmx.reg
        rts

hmx_cnd0
        move.l  d0,a2
hmx_cnam
        move.w  (a2)+,d0                 ; length of name
        bra.s   hmx_cnend
hmx_cnloop
        move.b  (a2)+,(a0)+              ; copy
hmx_cnend
        dbra    d0,hmx_cnloop
        rts
        end
