; Make item      V2.00     1990   Tony Tebby   QJUMP

        section exten

        xdef    hxt_mkit

        xref    gu_achpp

        include 'dev8_ee_hk_data'

;+++
; Make item: allocates memory.
; Fills item, including the name length, but not including the name characters.
;
;       d1 c  p item name length
;       d6 c  p (word) item type
;       a0  r   base of item
;       a2  r   pointer to characters on name
;       status return standard
;---
hxt_mkit
        moveq   #hki_name+2,d0           ; allocate heap item
        add.w   d1,d0

        jsr     gu_achpp                 ; allocate
        bne.s   hmi_exit

        move.l  a0,a2
        move.w  #hki.id,(a2)+            ; set id
        move.w  d6,(a2)+                 ; type of action
        clr.l   (a2)+                    ; pointer not set
        move.w  d1,(a2)+
        moveq   #0,d0

hmi_exit
        rts
        end
