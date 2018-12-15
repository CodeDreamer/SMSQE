; ATARI:  ROM header and bootloader

        section base

rom_header
        dc.l    $abcdEF42                ; magic
        dc.l    0                        ; only entry
        dc.l    $02fa0000+rom_hend+8-rom_header ; init address
        dc.l    0                        ; start address
        dc.l    0                        ; date and time - no meaning
        dc.l    0                        ; BSS size - no meaning
        dc.w    0                        ; no name

rom_hend
        end
