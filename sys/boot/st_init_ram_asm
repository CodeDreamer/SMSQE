; Initialise Atari ST

        section init

        xdef    init_ram

        include 'dev8_keys_atari'

;+++
; Atari INIT RAM
;
;     ( d7  r   map of .5Mbyte segments (up to 12 for Mega 6))
;       d7  r   number of .5Mbyte segments
;       a6 c  p return address
;---
init_ram
        moveq   #$0f,d7
        and.b   at_mconf,d7
        move.b  inram_tab(pc,d7.w),d7
        moveq   #0,d0
        jmp     (a6)

inram_tab
        dc.b    0,1,4,0
        dc.b    1,2,5,0
        dc.b    4,5,8,0
        dc.b    0,0,0,0

;        add.w   d7,d7
;        move.w  inram_tab(pc,d7.w),d7
;        moveq   #0,d0
;        jmp     (a6)
;
;inram_tab
;        dc.w    $0000,$0001,$000f,$0000
;        dc.w    $0001,$0003,$001f,$0000
;        dc.w    $000f,$001f,$00ff,$0000
;        dc.w    $0000,$0000,$0000,$0000

        end
