; HOTKEY Allocate New Hotkey   V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_newck                 ; new HOTKEY, check if in use
        xdef    hk_newst                 ; new HOTKEY, set it

        xref    cv_locas

        include 'dev8_keys_err'
        include 'dev8_ee_hk_data'
;+++
; This routine checks the table to see if a new HOTKEY can be allocated
; and allocates it
;
;       d1 cr   hotkey character
;       a1 c  p pointer to item
;       a3 c  p linkage block
;       error returns 0 or err.fdiu
;---
hk_newst
reglist reg     a2
        move.l  a2,-(sp)
        jsr     hkn_check                ; check if available
        bne.s   hkn_exit
        move.l  a1,-(a2)                 ; set pointer
        move.l  a2,d0
        lea     hkd_ptrb-4(a3),a2        ; find number
        sub.l   a2,d0
        lsr.w   #2,d0
        lea     hkd_tabl(a3),a2          ; and put into table
        move.b  d0,(a2,d1.w)
        moveq   #0,d0
        bra.s   hkn_exit

;+++
; This routine checks the table to see if a new HOTKEY can be allocated.
;
;       d1 cr   hotkey character
;       a3 c  p linkage block
;       error returns 0 or err.fdiu
;---
hk_newck
        move.l  a2,-(sp)
        bsr.s   hkn_check                ; do check
hkn_exit
        move.l  (sp)+,a2
        rts
        page

hkn_check
        lea     hkd_tabl(a3),a2
        tst.b   (a2,d1.w)                ; occupied?
        bne.s   hkn_inus

        lea     hkd_ptrb(a3),a2          ; look for empty hole
        moveq   #(hkd_ptrt-hkd_ptrb)/4-1,d0
hkn_look
        tst.l   (a2)+                    ; empty?
        dbeq    d0,hkn_look              ; ... no
        bne.s   hkn_inus
        moveq   #0,d0
        rts
hkn_inus
        moveq   #err.fdiu,d0
        rts
        end
