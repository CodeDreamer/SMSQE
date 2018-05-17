; HOTKEY REMOVE routine   V2.02     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_remv                   ; remove hotkey
        xdef    hk_remvc                  ; remove hotkey using character

        xref    hk_xname                  ; find name of executable Thing
        xref    hk_fitrm
        xref    hk_fitrc
        xref    gu_thjmp
        xref    gu_rchp

        include 'dev8_keys_qdos_sms'
        include 'dev8_ee_hk_data'
        include 'dev8_mac_assert'
;+++
; Remove hotkey ITEM, this always removes the key and pointer.
; For defined stuffer items, it also returns the ITEM to the common heap.
; For nop, execute file or pick, it also returns the ITEM to the common heap.
; For execute Thing items, it also returns the ITEM and the THING.
;
;       a1 c  p pointer to item name
;       a3 c  p linkage block
;
;---
hk_remv
reglist reg     d1/d2/a1/a2/a4
        movem.l reglist,-(sp)
        jsr     hk_fitrm                 ; find item and remove references
        bra.s   hkr_do

;+++
; Remove hotkey ITEM, this always removes the key and pointer.
; For defined stuffer items, it also returns the ITEM to the common heap.
; For nop, execute file or pick, it also returns the ITEM to the common heap.
; For execute Thing items, it also returns the ITEM and the THING.
;
;       d1 c  p hotkey
;       a3 c  p linkage block
;
;---
hk_remvc
        movem.l reglist,-(sp)
        jsr     hk_fitrc                 ; find item and remove references
hkr_do
        bne.s   hkr_exit
        move.l  a1,a4
        move.w  hki_type(a1),d1
        cmp.w   #hki.stuf,d1
        blt.s   hkr_exit                 ; ignore stuffer buffer and last line
        btst    #hki..trn,d1             ; transient thing?
        beq.s   hkr_ritm                 ; ... no, just remove item

        jsr     hk_xname                 ; using name
        moveq   #sms.zthg,d0             ; ... zap the thing
        jsr     gu_thjmp

hkr_ritm
        move.l  a4,a0
        jsr     gu_rchp                  ; return item to heap

hkr_exit
        movem.l (sp)+,reglist
        rts
        end
