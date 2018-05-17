* Check D1 against hit area   V1.00     1986  Tony Tebby   QJUMP
*
        section wman
*
        xdef    wm_chit                 check against (a2)
        xdef    wm_chitd                check against d4,d5
*
        include dev8_keys_err
*
*       d0  r   error return (0 or OR)
*       d1 c  p pointer x,y
*       d4 c  p hit area size
*       d5 c  p hit area origin
*       a2 c  p pointer to hit area: x,y size, x,y origin
*
*               all other registers preserved
*
xsize   equ     $00
ysize   equ     $02
xorg    equ     $04
yorg    equ     $06
*
wm_chit
        move.l  d1,d0                   first try y
        sub.w   yorg(a2),d0             off top?
        blt.s   wci_or                  ... yes
        cmp.w   ysize(a2),d0            off bottom?
        bge.s   wci_or                  ... yes
*
        swap    d0                      now try x
        sub.w   xorg(a2),d0             off left?
        blt.s   wci_or                  ... yes
        cmp.w   xsize(a2),d0            off right?
        bge.s   wci_or                  ... yes
        moveq   #0,d0                   ... no, it's in
        rts
*
wm_chitd
        move.l  d1,d0                   first try y
        sub.w   d5,d0                   off top?
        blt.s   wci_or                  ... yes
        cmp.w   d4,d0                   off bottom?
        bge.s   wci_or                  ... yes
*
        swap    d0                      now try x
        swap    d4
        swap    d5
        sub.w   d5,d0                   off left?
        blt.s   wci_sor                 ... yes
        cmp.w   d4,d0                   off right?
        bge.s   wci_sor                 ... yes
        swap    d4                      no, restore registers
        swap    d5
        moveq   #0,d0                   done
        rts
wci_sor
        swap    d4
        swap    d5
wci_or
        moveq   #err.orng,d0
        rts
        end
