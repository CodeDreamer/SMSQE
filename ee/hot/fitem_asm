; Procedure find Hotkey item   V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_fitem

        xref    hot_scpy
        xref    hk_fitem
;+++
; Find Hotkey item
;
;       d1  r   hotkey
;       d2  r   hotkey item number (-ve if off)
;       a1 cr   relative pointer to key or name / absolute pointer to item
;       a3 c  p thing address
;       status returns standard
;---
hot_fitem
        jsr     hot_scpy                 ; make pointer absolute
        jmp     hk_fitem                 ; and find item
        end
