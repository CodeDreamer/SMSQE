; HOTKEY Find ITEM   V2.01     1988   Tony Tebby   QJUMP
; first revision provides optional case independence

        section hotkey

        xdef    hk_fitem                 ; find item (a1) name or char
        xdef    hk_fitmu                 ; find item (a1) name or char (case)
        xdef    hk_fitrm                 ; find item (a1) and remove
        xdef    hk_fitmc                 ; find item d1 char
        xdef    hk_fitrc                 ; find item d1 and remove
        xdef    hk_fitmk                 ; find item d1 key (case optional)

        xref    hk_cname
        xref    cv_lctab

        include 'dev8_keys_k'
        include 'dev8_keys_err'
        include 'dev8_ee_hk_data'
        include 'dev8_mac_assert'

;+++
; This routine finds a hotkey item given a pointer to a name or key string
; and removes references from the hotkey table and pointer list
;
;       d1  r   (word) hotkey
;       d2  r   (word) hotkey number (-ve if off)
;       a1 cr   hotkey item name / pointer to hotkey item
;       a3 c  p linkage block
;       error returns 0 or err.fdnf
;---
hk_fitrm
reglist reg     d3/a0/a2/a4
        movem.l reglist,-(sp)
        bsr.s   hkf_itmu
        bra.s   hkf_remv
;+++
; This routine finds a hotkey item given a character
; and removes references from the hotkey table and pointer list
;
;       d1 cr   (word) hotkey
;       d2  r   (word) hotkey number (-ve if off)
;       a1  r   pointer to hotkey item
;       a3 c  p linkage block
;       error returns 0 or err.fdnf
;---
hk_fitrc
        movem.l reglist,-(sp)
        bsr.s   hkf_index
hkf_remv
        bne.s   hkf_exit
        clr.b   (a2)                     ; clear key
        clr.l   (a4)                     ; and pointer
        bra.s   hkf_exit
;+++
; This routine finds a hotkey item given a pointer to a name or key string
; Uppercase key is significant
;       d1  r   (word) hotkey
;       d2  r   (word) hotkey number (-ve if off)
;       a1 cr   hotkey item name / pointer to hotkey item
;       a3 c  p linkage block
;       error returns 0 or err.fdnf
;---
hk_fitmu
        movem.l reglist,-(sp)
        bsr.s   hkf_itmu
        bra.s   hkf_exit

;+++
; This routine finds a hotkey item given a pointer to a name or key string
;
;       d1  r   (word) hotkey
;       d2  r   (word) hotkey number (-ve if off)
;       a1 cr   hotkey item name / pointer to hotkey item
;       a3 c  p linkage block
;       error returns 0 or err.fdnf
;---
hk_fitem
        movem.l reglist,-(sp)
        moveq   #-1,d0                  ; case insignificant
        bsr.s   hkf_item
hkf_exit
        movem.l (sp)+,reglist
        rts

hkf_itmu
        moveq   #0,d0
hkf_item
        move.l  a1,a0
        move.w  (a1)+,d1
        cmp.w   #1,d1                    ; one character?
        beq.s   hkf_key                  ; ... just one

        moveq   #0,d1

hkf_loop
        bsr.s   hkf_index                ; index table
        bne.s   hkf_next                 ; ... next
        addq.l  #hki_name,a1
        jsr     hk_cname                 ; and compare
        subq.l  #hki_name,a1
        beq.s   hkfi_rts                 ; that's it
hkf_next
        addq.b  #1,d1                    ; next
        bne.s   hkf_loop

        moveq   #err.fdnf,d0             ; not found
hkfi_rts
        rts

;+++
; This routine finds a hotkey item given a hotkey character
;
;       d1 cr   (word) hotkey
;       d2  r   (word) hotkey number (-ve if off, 0 not found)
;       a1  r   pointer to hotkey item
;       a3 c  p linkage block
;       error returns 0 or err.fdnf
;---

hk_fitmc
        movem.l reglist,-(sp)
        bsr.s   hkf_index
        bra.s   hkf_exit

;+++
; This routine finds a hotkey item given a hotkey keystroke
;
;       d1 cr   (word) hotkey (may be lower cased)
;       d2  r   (word) hotkey number (-ve if off, 0 not found)
;       a1  r   pointer to hotkey item
;       a3 c  p linkage block
;       error returns 0 or err.fdnf
;---
hk_fitmk
        movem.l reglist,-(sp)
        bsr.s   hkf_kyck
        bra.s   hkf_exit

hkf_key
        move.b  (a1)+,d1                 ; key character
        tst.b   d0                       ; case significant?
        beq.s   hkf_index                ; ... yes
hkf_kyck
        and.w   #$00ff,d1
        bsr.s   hkf_index                ; try this case
        beq.s   hkfi_rts                 ; ... OK

        lea     cv_lctab,a2              ; try
        move.b  (a2,d1.w),d1             ; ... lowercased

        page
hkf_index
        lea     hkd_tabl(a3),a2          ; index the table
        add.w   d1,a2
        move.b  (a2),d2
        ext.w   d2                       ; a word
        move.w  d2,d0
        beq.s   hkf_nf                   ; ... not set
        bgt.s   hkf_pitem                ; ... ok
        neg.w   d0                       ; ... off

hkf_pitem
        lsl.w   #2,d0                    ; index the pointers
        lea     hkd_ptrb-4(a3),a4
        add.w   d0,a4
        move.l  (a4),a1                  ; pointer to item
        cmp.w   #hki.id,hki_id(a1)       ; is it item?
        bne.s   hkf_nf
        moveq   #0,d0
        rts

hkf_nf
        moveq   #err.fdnf,d0
        rts
        end
